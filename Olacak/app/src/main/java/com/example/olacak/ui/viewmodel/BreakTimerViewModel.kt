package com.example.olacak.ui.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.olacak.util.Constants
import com.github.mikephil.charting.utils.Utils.formatNumber
import dagger.hilt.android.lifecycle.HiltViewModel

class BreakTimerViewModel : ViewModel() {
    private var countDownTimer: CountDownTimer? = null
    private var remainingTime: Long = 0

    private val _breakTimerLiveData = MutableLiveData<String>()
    val breakTimerLiveData: LiveData<String> get() = _breakTimerLiveData

    private val _breakTimerFinished = MutableLiveData<Boolean>()
    val breakTimerFinished: LiveData<Boolean> get() = _breakTimerFinished

    fun startBreakTimer(breakTime: Long) {
        countDownTimer = object : CountDownTimer(breakTime, Constants.INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                _breakTimerLiveData.postValue(formatTimeValue(millisUntilFinished))
            }

            override fun onFinish() {
                _breakTimerFinished.postValue(true)
                vibrate()

            }
        }
        countDownTimer!!.start()
    }

    fun stopBreakTimer() {
        countDownTimer?.cancel()
    }


    fun resumeBreakTimer() {
        startBreakTimer(remainingTime)
    }

    private fun formatNumber(value: Long): String {
        if (value < 10)
            return "0$value"
        return "$value"
    }

    private fun formatTimeValue(time: Long): String {
        var seconds = time / 1000
        var minutes = seconds / 60
        val hours = minutes / 60
        if (minutes > 0)
            seconds %= 60
        if (hours > 0)
            minutes %= 60
        return formatNumber(hours) + ":" + formatNumber(minutes) + ":" +
                formatNumber(seconds)
    }

    private fun vibrate() {

    }



}

