package com.example.olacak.room
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.olacak.data.entity.TaskWorkTime
import com.example.olacak.data.entity.UserTask
import java.time.LocalDate

@Dao
interface UserTaskDao {

    @Insert
    suspend fun insert(userTask: UserTask)

    @Update
    suspend fun update(userTask: UserTask)

    @Delete
    suspend fun delete(userTask: UserTask)

    @Query("SELECT * FROM UserTask WHERE user_id = :userId AND task_date = :date")
    suspend fun getTasksByDay(userId: Long, date: String): List<UserTask>

    @Query("SELECT SUM(duration) FROM UserTask WHERE task_id = :taskId")
    suspend fun getTotalDurationForTask(taskId: Long): Long

    @Query("INSERT INTO UserTask (user_id, task_id, duration, task_date) VALUES (:userId, :taskId, :duration, :date)")
    suspend fun addTaskForUser(userId: Long, taskId: Long, duration: Long, date: String)

    @Query("""
        SELECT taskName AS taskName, SUM(workTime) AS totalWorkTime 
        FROM Pomodoro 
        WHERE userId = :userId 
        GROUP BY taskName""")
    suspend fun getTotalWorkTimeByTask(userId: Long): List<TaskWorkTime>


    @Query("SELECT * FROM UserTask WHERE user_id = :userId AND task_date BETWEEN :startDate AND :endDate")
    suspend fun getTasksByDateRange(userId: Long, startDate: String, endDate: String): List<UserTask>


    @Query("SELECT * FROM UserTask WHERE user_id = :userId")
    suspend fun userTasks(userId: Long): List<UserTask>
}









