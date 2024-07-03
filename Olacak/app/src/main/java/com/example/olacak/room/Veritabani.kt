package com.example.olacak.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.olacak.data.entity.Gorevler
import com.example.olacak.data.entity.Pomodoro
import com.example.olacak.data.entity.User
import com.example.olacak.data.entity.UserTask


@Database(entities = [Gorevler::class, User::class, UserTask::class, Pomodoro::class], version = 5, exportSchema = false)
abstract class Veritabani: RoomDatabase(){

    abstract fun gorevlerDao(): GorevlerDao
    abstract fun userDao(): UserDao
    abstract fun userTaskDao(): UserTaskDao
    abstract fun pomodoroDao(): PomodoroDao

    companion object {
        @Volatile
        private var INSTANCE: Veritabani? = null

        fun getDatabase(context: Context): Veritabani {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Veritabani::class.java,
                    "todolist.sqlite"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}