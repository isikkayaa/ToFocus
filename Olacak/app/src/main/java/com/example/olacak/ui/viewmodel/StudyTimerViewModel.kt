package com.example.olacak.ui.viewmodel

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olacak.R
import com.example.olacak.data.entity.Gorevler
import com.example.olacak.data.entity.Pomodoro
import com.example.olacak.data.entity.TaskWorkTime
import com.example.olacak.data.repo.GorevlerRepository
import com.example.olacak.data.repo.RepositoryImpl
import com.example.olacak.util.Constants
import com.example.olacak.util.VibratorHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class StudyTimerViewModel @Inject constructor(
    private val repository: RepositoryImpl,
    @ApplicationContext private val context: Context,
    var grepo: GorevlerRepository
) : ViewModel() {

    private var countDownTimer: CountDownTimer? = null
    var remainingTime: Long = 0
    var pausedTime: Long = 0
    var repeatTime: Int = 0
    private val vibrator = VibratorHelper.from(context)
    private var mediaPlayer: MediaPlayer? = null

    private var _pomodoroTextLiveData = MutableLiveData<String>()
    val pomodoroTextLiveData: LiveData<String> get() = _pomodoroTextLiveData

    private var _workTimerFinished = MutableLiveData(false)
    val workTimerFinished: LiveData<Boolean> get() = _workTimerFinished

    private var _pomodoroRepeatTime = MutableLiveData(0)
    val pomodoroRepeatTime: LiveData<Int> get() = _pomodoroRepeatTime

    private var _taskWorkTimeList = MutableLiveData<List<TaskWorkTime>>()
    val taskWorkTimeList: LiveData<List<TaskWorkTime>> get() = _taskWorkTimeList

    private var _taskNames = MutableLiveData<List<String>>()
    val taskNames: LiveData<List<String>> get() = _taskNames
    private var userId: Long = getUserIdFromSharedPreferences()

    init {
        loadTaskWorkTimes()
        loadTaskNames()
    }

    fun loadTaskNames() {
        viewModelScope.launch(Dispatchers.IO) {
            val tasks = grepo.getUserTasks(userId)
            _taskNames.postValue(tasks.map { it.gorev_adi })
        }
    }

    fun addNewTask(gorev_adi: String, gorev_aciklamasi: String) {
        viewModelScope.launch(Dispatchers.IO) {
            grepo.kaydet(gorev = Gorevler(0,"","",0))
            loadTaskNames()
        }
    }



    fun loadTaskWorkTimes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = getUserIdFromSharedPreferences()
                val taskWorkTimes = repository.getTotalWorkTimeByTask(userId)
                _taskWorkTimeList.postValue(taskWorkTimes)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "Error loading task work times: ${e.message}")
            }
        }
    }

    fun startTimerForWork() {
        val duration = getTimerDurationFromPreferences()
        startCountDownTimer(duration)
    }

    fun stopCountDownTimer() {
        countDownTimer?.cancel()
    }

    fun resumeCountDownTimer() {
        startCountDownTimer(pausedTime)
    }

    fun resetCountDownTimer() {
        countDownTimer?.cancel()
        remainingTime = getTimerDurationFromPreferences()
        pausedTime = remainingTime
        _pomodoroTextLiveData.postValue(formatTimeValue(remainingTime))
        startTimerForWork()
    }

    fun insertAndUpdatePomodoro(pomodoro: Pomodoro) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insertAndUpdate(pomodoro)
                Log.d(Constants.TAG, "Pomodoro successfully inserted/updated")
                loadTaskWorkTimes() // Update the chart data after inserting new pomodoro
            } catch (e: Exception) {
                Log.d(Constants.TAG, "insertAndUpdatePomodoro: ${e.message}")
            }
        }
    }

    private fun startCountDownTimer(duration: Long) {
        countDownTimer = object : CountDownTimer(duration, Constants.INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                pausedTime = millisUntilFinished
                _pomodoroTextLiveData.postValue(formatTimeValue(millisUntilFinished))
            }

            override fun onFinish() {
                if (duration != getUserWorkTime()) {
                    _workTimerFinished.postValue(false)
                } else {
                    _workTimerFinished.postValue(true)
                    repeatTime += 1
                    _pomodoroRepeatTime.postValue(repeatTime)
                }
                vibrate()
                playAlarm("studyAlarm")
            }
        }
        countDownTimer!!.start()
    }

    private fun formatNumber(value: Long): String {
        if (value < 10) return "0$value"
        return "$value"
    }

    private fun formatTimeValue(time: Long): String {
        var seconds = time / 1000
        var minutes = seconds / 60
        val hours = minutes / 60
        if (minutes > 0) seconds %= 60
        if (hours > 0) minutes %= 60
        return formatNumber(hours) + ":" + formatNumber(minutes) + ":" + formatNumber(seconds)
    }

    private fun vibrate() {
        vibrator?.vibrate()
    }



    private fun getTimerDurationFromPreferences(): Long {
        val sharedPreferences = context.getSharedPreferences("PomodoroSettings", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("study_timer", 1 * 60 * 1000L)
    }

    private fun saveTimerDurationToPreferences(studyDuration: Long, breakDuration: Long) {
        val sharedPreferences = context.getSharedPreferences("PomodoroSettings", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putLong("study_timer", studyDuration)
            putLong("break_timer", breakDuration)
            apply()
        }
    }

    private fun playAlarm(alarmKey: String) {
        val sharedPreferences = context.getSharedPreferences("PomodoroSettings", Context.MODE_PRIVATE)
        val alarm = sharedPreferences.getString(alarmKey, "Alarm 1")
        val resId = when (alarm) {
            "Alarm 1" -> R.raw.study_sound
            "Alarm 2" -> R.raw.alarm2
            "Alarm 3" -> R.raw.alarm3
            "Alarm 4" -> R.raw.alarm4
            "Alarm 5" -> R.raw.alarm5
            "Alarm 6" -> R.raw.alarm6
            "Alarm 7" -> R.raw.alarm7
            "Alarm 8" -> R.raw.alarm8
            "Alarm 9" -> R.raw.alarm9
            "Alarm 10" -> R.raw.alarm10
            "Alarm 11" -> R.raw.alarm11
            "Alarm 12" -> R.raw.alarm12
            "Alarm 13" -> R.raw.alarm13
            "Alarm 14" -> R.raw.alarm14
            "Alarm 15" -> R.raw.alarm15
            "Alarm 16" -> R.raw.alarm16
            "Alarm 17" -> R.raw.alarm17
            "Alarm 18" -> R.raw.alarm18
            "Alarm 19" -> R.raw.alarm19
            "Alarm 20" -> R.raw.alarm20
            "Alarm 21" -> R.raw.alarm21
            "Alarm 22" -> R.raw.alarm22
            "Alarm 23" -> R.raw.alarm23
            "Alarm 24" -> R.raw.alarm24
            "Alarm 25" -> R.raw.break_sound
            else -> R.raw.study_sound
        }

        mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer?.start()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
    }

    fun getUserIdFromSharedPreferences(): Long {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("user_id", -1)
    }

    fun getUserWorkTime(): Long {
        val sharedPreferences = context.getSharedPreferences("PomodoroSettings", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("study_timer", 2 * 60 * 1000L) // Varsayılan 40 dakika
    }



}



