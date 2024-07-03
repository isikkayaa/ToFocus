package com.example.olacak.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.olacak.data.entity.Gorevler

@Dao
interface GorevlerDao {


    @Query("SELECT * FROM Gorevler WHERE user_id = :userId")
    suspend fun getUserTasks(userId: Long): List<Gorevler>


    @Query("SELECT * FROM Gorevler WHERE gorev_id IN (:taskIds)")
    suspend fun getTasksByIds(taskIds: List<Long>): List<Gorevler>

    @Query("SELECT gorev_adi FROM Gorevler")
    suspend fun getAllTaskNames(): List<String>

    @Query("SELECT * FROM Gorevler WHERE gorev_id = :taskId")
    suspend fun getTaskById(taskId: Long): Gorevler


    @Insert
    suspend fun kaydet(gorev: Gorevler)

    @Update
    suspend fun guncelle(gorev: Gorevler)

    @Delete
    suspend fun sil(gorev: Gorevler)



}