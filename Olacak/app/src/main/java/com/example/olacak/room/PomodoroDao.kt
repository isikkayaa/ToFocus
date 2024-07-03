package com.example.olacak.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.olacak.data.entity.Pomodoro
import com.example.olacak.data.entity.TaskWorkTime
import com.example.olacak.data.entity.TaskWorkTimeByDay
import com.example.olacak.data.entity.TaskWorkTimeByWeek
import java.time.LocalDateTime

@Dao
interface PomodoroDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPomodoro(pomodoro: Pomodoro)

    @Query("SELECT * FROM Pomodoro WHERE userId = :userId")
    suspend fun getPomodoros(userId: Long): List<Pomodoro>

    @Query("SELECT taskName, SUM(workTime) as totalWorkTime FROM Pomodoro WHERE userId = :userId GROUP BY taskName")
    suspend fun getTotalWorkTimeByTask(userId: Long): List<TaskWorkTime>



    @Query("SELECT taskName, SUM(workTime) as totalWorkTime, strftime('%w', datetime(createdAt / 1000, 'unixepoch')) as dayOfWeek FROM Pomodoro WHERE userId = :userId AND createdAt BETWEEN :startDate AND :endDate GROUP BY taskName, dayOfWeek")
    suspend fun getTotalWorkTimeByDayOfWeek(userId: Long, startDate: Long, endDate: Long): List<TaskWorkTimeByDay>

    @Query("SELECT taskName, SUM(workTime) as totalWorkTime, strftime('%W', datetime(createdAt / 1000, 'unixepoch')) as weekOfYear FROM Pomodoro WHERE userId = :userId AND createdAt BETWEEN :startDate AND :endDate GROUP BY weekOfYear, taskName")
    suspend fun getTotalWorkTimeByWeekOfYear(userId: Long, startDate: Long, endDate: Long): List<TaskWorkTimeByWeek>


    @Query("SELECT * FROM Pomodoro WHERE strftime('%Y-%m', datetime(createdAt / 1000, 'unixepoch')) = :yearMonth")
    suspend fun getPomodorosByYearMonth(yearMonth: String): List<Pomodoro>

    @Query("SELECT * FROM Pomodoro WHERE id = :taskId")
    suspend fun getPomodoroById(taskId: Long): Pomodoro

    @Query("SELECT * FROM Pomodoro WHERE createdAt BETWEEN :startDate AND :endDate")
    suspend fun getPomodorosBetweenDates(startDate: Long, endDate: Long): List<Pomodoro>
}



