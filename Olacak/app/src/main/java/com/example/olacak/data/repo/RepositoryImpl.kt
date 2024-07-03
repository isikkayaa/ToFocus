package com.example.olacak.data.repo

import android.graphics.Paint.Style
import com.example.olacak.data.entity.DailyWorkTime
import com.example.olacak.data.entity.Pomodoro
import com.example.olacak.data.entity.TaskWorkTime
import com.example.olacak.data.entity.TaskWorkTimeByDay
import com.example.olacak.data.entity.TaskWorkTimeByWeek
import com.example.olacak.room.GorevlerDao
import com.example.olacak.room.PomodoroDao
import com.example.olacak.room.PomodoroTypeConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val pomodoroDao: PomodoroDao,
    private val userTaskRepository: UserTaskRepository,private val gorevlerDao: GorevlerDao
) {
    suspend fun insertAndUpdate(pomodoro: Pomodoro) {
        withContext(Dispatchers.IO) {
            pomodoroDao.insertPomodoro(pomodoro)
        }
    }

    suspend fun getAllPomodoros(): List<Pomodoro> = withContext(Dispatchers.IO) {
        pomodoroDao.getPomodoros(userId = 0)
    }

    suspend fun getTasksByDay(userId: Long, date: String): List<TaskWorkTime> {
        return withContext(Dispatchers.IO) {
            val tasks = userTaskRepository.getTasksByDay(userId, date)
            tasks.map { task ->
                val taskName = gorevlerDao.getTaskById(task.taskId)?.gorev_adi ?: "Unknown Task"
                TaskWorkTime(taskName, task.duration)
            }
        }
    }
    suspend fun getTotalWorkTimeByTask(userId: Long): List<TaskWorkTime> {
        return withContext(Dispatchers.IO) {
            pomodoroDao.getTotalWorkTimeByTask(userId)
        }
    }




    suspend fun getTasksByDateRange(userId: Long, startDate: LocalDate, endDate: LocalDate): List<TaskWorkTime> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val startDateString = startDate.format(formatter)
        val endDateString = endDate.format(formatter)
        return withContext(Dispatchers.IO) {
            val tasks = userTaskRepository.getTasksByDateRange(userId, startDateString, endDateString)
            tasks.map { task ->
                val taskName = gorevlerDao.getTaskById(task.taskId)?.gorev_adi ?: "Unknown Task"
                TaskWorkTime(taskName, task.duration)
            }
        }
    }


    suspend fun getTotalWorkTimeByDayOfWeekWithNames(userId: Long, startDate: LocalDate, endDate: LocalDate): List<DailyWorkTime> {
        val startEpochSecond = startDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
        val endEpochSecond = endDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
        val dailyWorkTimes = pomodoroDao.getTotalWorkTimeByDayOfWeek(userId, startEpochSecond, endEpochSecond)

        val dayNames = getWeekDays(startDate, endDate)
        val result = mutableListOf<DailyWorkTime>()


        for (i in 0 until 7) {
            val dayName = dayNames[i]
            val workTimeOfDay = dailyWorkTimes.find { dailyWorkTime -> dailyWorkTime.dayOfWeek.toInt() == i }

            val totalWorkTime = workTimeOfDay?.totalWorkTime ?: 0L
            result.add(DailyWorkTime(dayName, totalWorkTime))

        }

        return result
    }

    private fun getWeekDays(startDate: LocalDate, endDate: LocalDate): List<String> {
        val weekDays = arrayListOf<String>()
        val formatter = DateTimeFormatter.ofPattern("E")
        var currentDay = startDate
        while (!currentDay.isAfter(endDate)) {
            weekDays.add(currentDay.format(formatter))
            currentDay = currentDay.plusDays(1)
        }
        return weekDays
    }


    suspend fun getTotalWorkTimeByDayOfWeek(userId: Long, startDate: LocalDate, endDate: LocalDate): List<TaskWorkTimeByDay> {
        val startEpochSecond = startDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
        val endEpochSecond = endDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
        return withContext(Dispatchers.IO) {
            pomodoroDao.getTotalWorkTimeByDayOfWeek(userId, startEpochSecond, endEpochSecond)
        }
    }

    suspend fun getTotalWorkTimeByWeekOfYear(userId: Long, startDate: LocalDate, endDate: LocalDate): List<TaskWorkTimeByWeek> {
        val startEpochSecond = startDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
        val endEpochSecond = endDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
        return withContext(Dispatchers.IO) {
            pomodoroDao.getTotalWorkTimeByWeekOfYear(userId, startEpochSecond, endEpochSecond)
        }
    }
}
