package com.example.olacak.data.repo

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.olacak.data.entity.UserTask
import com.example.olacak.room.UserTaskDao
import java.time.LocalDate
import javax.inject.Inject

class UserTaskRepository @Inject constructor(private val userTaskDao: UserTaskDao) {

    suspend fun getTasksByDay(userId: Long, date: String): List<UserTask> {
        return userTaskDao.getTasksByDay(userId, date)
    }

    suspend fun getTasksByDateRange(userId: Long, startDate: String, endDate: String): List<UserTask> {
        return userTaskDao.getTasksByDateRange(userId, startDate, endDate)
    }

    suspend fun userTasks(userId: Long): List<UserTask> {
        return userTaskDao.userTasks(userId)
    }

    suspend fun getTotalDurationForTask(taskId: Long): Long {
        return userTaskDao.getTotalDurationForTask(taskId)
    }

    suspend fun insert(userTask: UserTask) {
        userTaskDao.insert(userTask)
    }

    suspend fun update(userTask: UserTask) {
        userTaskDao.update(userTask)
    }

    suspend fun delete(userTask: UserTask) {
        userTaskDao.delete(userTask)
    }

    suspend fun addTaskForUser(userId: Long, taskId: Long, duration: Long,taskDate:String) {
        val userTask = UserTask(
            userTaskId = 0,
            userId = userId,
            taskId = taskId,
            duration = duration,
            taskDate = taskDate
        )
        insert(userTask)
    }
}
