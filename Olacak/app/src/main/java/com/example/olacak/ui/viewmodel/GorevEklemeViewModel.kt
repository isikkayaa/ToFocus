package com.example.olacak.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olacak.data.entity.Gorevler
import com.example.olacak.data.repo.GorevlerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GorevEklemeViewModel @Inject constructor(private val grepo: GorevlerRepository) : ViewModel() {

    fun kaydet(gorev_adi: String, gorev_aciklamasi: String, user_id: Long) {
        viewModelScope.launch {
            val gorev = Gorevler(gorev_id = 0, gorev_adi = gorev_adi, gorev_aciklamasi = gorev_aciklamasi, user_id = user_id)
            try {
                grepo.kaydet(gorev)
            } catch (e: Exception) {
                Log.e("GorevEklemeViewModel", "Error saving task", e)
            }
        }
    }
}
