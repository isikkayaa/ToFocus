package com.example.olacak.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olacak.data.entity.Gorevler
import com.example.olacak.data.entity.Pomodoro
import com.example.olacak.data.entity.TaskWorkTime
import com.example.olacak.data.entity.TaskWorkTimeByDay
import com.example.olacak.data.entity.TaskWorkTimeByWeek
import com.example.olacak.data.repo.GorevlerRepository
import com.example.olacak.data.repo.RepositoryImpl
import com.example.olacak.data.repo.UserTaskRepository
import com.example.olacak.util.Constants
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class AnasayfaViewModel @Inject constructor(
    private val grepo: GorevlerRepository,
    private val repository: RepositoryImpl,
    private val userTaskRepository: UserTaskRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val userName = MutableLiveData<String>()
    val profileImageUri = MutableLiveData<Uri?>()
    private val sharedPreferences = context.getSharedPreferences("PomodoroSettings", Context.MODE_PRIVATE)

    private val _todayTasks = MutableLiveData<List<TaskWorkTime>>()
    val todayTasks: LiveData<List<TaskWorkTime>> = _todayTasks

    private val _gorevlerListesi = MutableLiveData<List<Gorevler>>()
    val gorevlerListesi: LiveData<List<Gorevler>> get() = _gorevlerListesi




    private val _pomodoroBarEntriesLiveData = MutableLiveData<List<BarEntry>>()
    val _pomodoroLabelsLiveData = MutableLiveData<List<String>>()
    private var _pomodoroChartData = MediatorLiveData<Pair<List<BarEntry>, List<String>>>()
    val pomodoroChartData: MediatorLiveData<Pair<List<BarEntry>, List<String>>> get() = _pomodoroChartData


    private val userId: Long by lazy { getUserIdFromSharedPreferences() }
    private val barEntries = ArrayList<BarEntry>()
    private val labels = ArrayList<String>()



    init {
        loadProfileData()
        getUserTasks()
        loadChartData()
        loadTodayTasks(userId)
        pomodoroChartData.addSource(_pomodoroBarEntriesLiveData) { value ->
            pomodoroChartData.value = Pair(value, labels)
        }
        pomodoroChartData.addSource(_pomodoroLabelsLiveData) { value ->
            pomodoroChartData.value = Pair(barEntries, value)
        }


    }

    private fun loadProfileData() {
        userName.value = sharedPreferences.getString("user_name", "")
        profileImageUri.value = sharedPreferences.getString("profile_image_uri", null)?.let { Uri.parse(it) }
    }



    fun loadTodayTasks(userId: Long) {
        clearData()
        viewModelScope.launch {
            val today = LocalDate.now()
            val dailyWorkTimes = repository.getTotalWorkTimeByDayOfWeek(userId, today, today)

            val workTimesByTask = mutableMapOf<String, Float>()

            dailyWorkTimes.forEach { taskWorkTimeByDay ->
                workTimesByTask[taskWorkTimeByDay.taskName] = workTimesByTask.getOrDefault(taskWorkTimeByDay.taskName, 0f) + taskWorkTimeByDay.totalWorkTime.toFloat() / 60000f
            }

            val todayTasks = workTimesByTask.map { (taskName, totalWorkTime) ->
                TaskWorkTime(
                    taskName = taskName,
                    totalWorkTime = totalWorkTime.toLong()
                )
            }

            _todayTasks.postValue(todayTasks)
        }
    }




    fun getUserTasks() {
        viewModelScope.launch {
            val userTasks = userTaskRepository.userTasks(userId)
            val taskIds = userTasks.map { it.taskId }
            val tasks = grepo.getUserTasks(userId)
            _gorevlerListesi.value = tasks
        }
    }


    fun loadUserTasks() {
        viewModelScope.launch {
            _gorevlerListesi.value = grepo.getUserTasks(userId)
        }
    }






    fun loadChartData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val taskWorkTimes = repository.getTotalWorkTimeByTask(userId)
                barEntries.clear()
                labels.clear()
                taskWorkTimes.forEach { taskWorkTime ->
                    barEntries.add(BarEntry(barEntries.size.toFloat(), taskWorkTime.totalWorkTime.toFloat() / 60000f)) // Milliseconds to minutes
                    labels.add(taskWorkTime.taskName)
                }

                _pomodoroBarEntriesLiveData.postValue(barEntries)
                _pomodoroLabelsLiveData.postValue(labels)

            } catch (e: Exception) {
                Log.e("AnasayfaViewModel", "loadChartData: ${e.message}")
            }
        }}



    private fun getUserIdFromSharedPreferences(): Long {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("user_id", -1)
    }


    private fun clearData() {
        barEntries.clear()
        labels.clear()


    }

    fun sil(gorevId: Long) {
        viewModelScope.launch {
            grepo.sil(gorevId)
            getUserTasks()
        }
    }
}


