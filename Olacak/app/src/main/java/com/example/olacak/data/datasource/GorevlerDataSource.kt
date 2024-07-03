package com.example.olacak.data.datasource

import android.util.Log
import com.example.olacak.data.entity.Gorevler
import com.example.olacak.room.GorevlerDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GorevlerDataSource(var gdao: GorevlerDao) {

    suspend fun getUserTasks(userId: Long): List<Gorevler> {
        return gdao.getUserTasks(userId)
    }

    suspend fun getTasksByIds(taskIds: List<Long>): List<Gorevler> {
        return gdao.getTasksByIds(taskIds)
    }

    suspend fun kaydet(gorev: Gorevler) {
        try {
            gdao.kaydet(gorev)
        } catch (e: Exception) {
            Log.e("GorevlerDataSource", "Error saving task", e)
        }
    }



    suspend fun guncelle(gorev: Gorevler) {
        gdao.guncelle(gorev)
    }

    suspend fun sil(gorev_id: Long) {
        val silinenGorev = Gorevler(gorev_id, "", "", 0)
        gdao.sil(silinenGorev)
    }


}