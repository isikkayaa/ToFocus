package com.example.olacak.di

import android.content.Context
import androidx.room.Room
import com.example.olacak.data.datasource.GorevlerDataSource
import com.example.olacak.data.repo.GorevlerRepository
import com.example.olacak.data.repo.RepositoryImpl
import com.example.olacak.data.repo.UserTaskRepository
import com.example.olacak.room.GorevlerDao
import com.example.olacak.room.PomodoroDao
import com.example.olacak.room.UserDao
import com.example.olacak.room.UserTaskDao
import com.example.olacak.room.Veritabani
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): Veritabani =
        Room.databaseBuilder(context, Veritabani::class.java, "todolist.sqlite")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideGorevlerDao(db: Veritabani): GorevlerDao = db.gorevlerDao()

    @Singleton
    @Provides
    fun provideUserDao(db: Veritabani): UserDao = db.userDao()

    @Singleton
    @Provides
    fun provideUserTaskDao(db: Veritabani): UserTaskDao = db.userTaskDao()

    @Singleton
    @Provides
    fun providePomodoroDao(db: Veritabani): PomodoroDao = db.pomodoroDao()

    @Provides
    @Singleton
    fun provideGorevlerDataSource(gdao: GorevlerDao): GorevlerDataSource {
        return GorevlerDataSource(gdao)
    }

    @Provides
    @Singleton
    fun provideGorevlerRepository(gds: GorevlerDataSource): GorevlerRepository {
        return GorevlerRepository(gds)
    }

    @Provides
    @Singleton
    fun provideUserTaskRepository(utd: UserTaskDao): UserTaskRepository {
        return UserTaskRepository(utd)
    }

    @Provides
    @Singleton
    fun provideRepositoryImpl(pomodoroDao: PomodoroDao,userTaskRepository: UserTaskRepository,gorevlerDao: GorevlerDao): RepositoryImpl {
        return RepositoryImpl(pomodoroDao,userTaskRepository,gorevlerDao)
    }




}

