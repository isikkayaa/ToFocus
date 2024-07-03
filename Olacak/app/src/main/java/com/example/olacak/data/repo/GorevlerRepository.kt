package com.example.olacak.data.repo

import com.example.olacak.data.datasource.GorevlerDataSource
import com.example.olacak.data.entity.Gorevler

class GorevlerRepository(var gds: GorevlerDataSource) {

    suspend fun kaydet(gorev: Gorevler) {
      gds.kaydet(gorev)
    }

    suspend fun guncelle(gorev: Gorevler) {
        gds.guncelle(gorev)
    }

    suspend fun sil(gorev_id: Long) {
        gds.sil(gorev_id)
    }

    suspend fun getUserTasks(userId: Long): List<Gorevler> {
        return gds.getUserTasks(userId)
    }

    suspend fun getTasksByIds(taskIds: List<Long>): List<Gorevler> {
        return gds.getTasksByIds(taskIds)
    }




}
