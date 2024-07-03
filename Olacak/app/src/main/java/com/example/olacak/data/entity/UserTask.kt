package com.example.olacak.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.olacak.room.PomodoroTypeConverter
import org.jetbrains.annotations.NotNull
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "UserTask",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Gorevler::class,
            parentColumns = ["gorev_id"],
            childColumns = ["task_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["user_id"]), Index(value = ["task_id"])]
)
@TypeConverters(PomodoroTypeConverter::class)
data class UserTask(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_task_id")
    var userTaskId: Long = 0,
    @ColumnInfo(name = "user_id")
    val userId: Long,
    @ColumnInfo(name = "task_id")
    val taskId: Long,
    @ColumnInfo(name = "duration")
    val duration: Long,
    @ColumnInfo(name = "task_date")
    val taskDate: String
) : Serializable {
}



