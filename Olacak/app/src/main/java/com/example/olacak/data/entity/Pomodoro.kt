package com.example.olacak.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.olacak.room.PomodoroTypeConverter
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.time.LocalDateTime

@Entity(tableName = "Pomodoro")
@TypeConverters(PomodoroTypeConverter::class)
data class Pomodoro(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id") var id:Long = 0,
    @ColumnInfo(name = "userId") var userId: Long,
    @ColumnInfo(name = "workTime") var workTime: Long,
    @ColumnInfo(name= "taskName") var taskName:String,
    @ColumnInfo(name = "createdAt") var createdAt: LocalDateTime = LocalDateTime.now()) :Serializable  {

    }


