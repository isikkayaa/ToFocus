package com.example.olacak.room


import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


object PomodoroTypeConverter {
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): Long? {
        return value?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }

    @TypeConverter
    fun toLocalDateTime(value: Long?): LocalDateTime? {
        return value?.let { Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime() }
    }
}



