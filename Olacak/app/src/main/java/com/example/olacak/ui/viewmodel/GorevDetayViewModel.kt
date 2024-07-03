package com.example.olacak.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
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
class GorevDetayViewModel @Inject constructor(var grepo: GorevlerRepository) : ViewModel() {


    fun guncelle(gorev_id:Long, gorev_adi:String, gorev_aciklamasi:String,user_id:Long) {
        viewModelScope.launch {
            val gorev = Gorevler(gorev_id, gorev_adi, gorev_aciklamasi, user_id)
            try {
                grepo.guncelle(gorev)

            } catch (e:Exception){
                Log.e("GorevDetayViewModel", "Error updating task", e)
            }
        }
    }

}