package com.example.olacak.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.olacak.R
import com.example.olacak.data.entity.DailyWorkTime
import com.example.olacak.data.entity.Gorevler
import com.example.olacak.data.entity.TaskWorkTime
import com.example.olacak.databinding.FragmentAnasayfaBinding
import com.example.olacak.room.GorevlerDao
import com.example.olacak.room.PomodoroDao
import com.example.olacak.room.UserDao
import com.example.olacak.room.UserTaskDao
import com.example.olacak.room.Veritabani
import com.example.olacak.ui.viewmodel.AnasayfaViewModel
import com.example.olacak.util.Constants
import com.example.olacak.util.MinutesValueFormatter
import com.example.olacak.util.gecisYap
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.collections.emptyList


@AndroidEntryPoint
class AnasayfaFragment : Fragment() {

    private var _binding: FragmentAnasayfaBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AnasayfaViewModel
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var barChart: BarChart
    private lateinit var gorevlerDao: GorevlerDao
    private lateinit var userTaskDao: UserTaskDao
    private lateinit var userDao: UserDao
    private lateinit var pomodoroDao: PomodoroDao
    private lateinit var listAdapter: com.example.olacak.ui.adapter.ListAdapter
    private var userId: Long = -1
    private lateinit var weekDayLabels: List<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AnasayfaViewModel::class.java)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_anasayfa, container, false)
        binding.anasayfaFragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initializeDatabaseDaos()
        setupRecyclerView()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        barChart = binding.barChart

        viewModel.loadTodayTasks(userId)
        viewModel.loadChartData()

        setupBarChart()
        userId = getUserIdFromSharedPreferences(requireContext())
        viewModel.loadUserTasks()
        viewModel.loadTodayTasks(userId)

        observeViewModel()


        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserTasks()
    }

    private fun initializeDatabaseDaos() {
        val database = Veritabani.getDatabase(requireContext())
        gorevlerDao = database.gorevlerDao()
        userTaskDao = database.userTaskDao()
        userDao = database.userDao()
        pomodoroDao = database.pomodoroDao()
    }

    private fun setupRecyclerView() {
        listAdapter = com.example.olacak.ui.adapter.ListAdapter(requireContext(), emptyList(), viewModel)
        binding.rv.adapter = listAdapter

        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.rv.layoutManager = layoutManager
    }

    private fun observeViewModel() {
        viewModel.gorevlerListesi.observe(viewLifecycleOwner) { gorevlerListesi ->
            listAdapter.updateGorevlerList(gorevlerListesi)
        }

        viewModel.pomodoroChartData.observe(viewLifecycleOwner, Observer { (entries, labels) ->
            updateBarChart(entries, labels)
        })

        viewModel.todayTasks.observe(viewLifecycleOwner) { tasks ->
            val entries = tasks.mapIndexed { index, task ->
                BarEntry(index.toFloat(), task.totalWorkTime.toFloat())
            }
            updateBarChart(entries, tasks.map { it.taskName })
        }

        viewModel.weekTasks.observe(viewLifecycleOwner) { tasks ->
            if (::weekDayLabels.isInitialized) {
                val entries = tasks.mapIndexed { index, task ->
                    BarEntry(index.toFloat(), task.totalWorkTime.toFloat())
                }
                updateBarChart(entries, weekDayLabels)
            } else {
                Log.e("AnasayfaFragment", "weekDayLabels has not been initialized")
                val defaultLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                val entries = tasks.mapIndexed { index, task ->
                    BarEntry(index.toFloat(), task.totalWorkTime.toFloat())
                }
                updateBarChart(entries, defaultLabels)
            }
        }


        viewModel.monthTasks.observe(viewLifecycleOwner) { tasks ->
            val weekLabels = viewModel._pomodoroLabelsLiveData.value ?: listOf("Week 1", "Week 2", "Week 3", "Week 4")
            val entries = tasks.mapIndexed { index, task ->
                BarEntry(index.toFloat(), task.totalWorkTime.toFloat())
            }
            updateBarChart(entries, weekLabels)
        }


    }





    private fun getWeekDays(startDate: LocalDate, endDate: LocalDate): List<String> {
        val weekDays = arrayListOf<String>()
        val formatter = DateTimeFormatter.ofPattern("E")
        var currentDay = startDate
        while (!currentDay.isAfter(endDate)) {
            weekDays.add(currentDay.format(formatter))
            currentDay = currentDay.plusDays(1)
        }
        return weekDays
    }






    private fun setupClickListeners() {
        binding.todayBtn.setOnClickListener {
            viewModel.loadTodayTasks(userId)
            viewModel.loadChartData()
        }


        binding.fab.setOnClickListener { fabTikla(it) }
    }




    private fun setupBarChart() {
        barChart.apply {
            description.isEnabled = false
            setDrawValueAboveBar(true)
            axisLeft.axisMinimum = 0f
            axisRight.isEnabled = false
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                setDrawGridLines(false)
            }
            legend.isEnabled = false
        }
    }
    private fun updateBarChart(entries: List<BarEntry>, labels: List<String>) {
        val dataSet = BarDataSet(entries, "Work Time")
        dataSet.color = resources.getColor(R.color.toolbar, null)
        val barData = BarData(dataSet)
        barData.setValueTextSize(12f)
        barData.barWidth = 0.6f


        barData.setValueFormatter(MinutesValueFormatter())

        barChart.data = barData
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.xAxis.granularity = 1f
        barChart.axisLeft.granularity = 20f
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisLeft.axisMaximum = 180f
        barChart.invalidate()
    }




    private fun getUserIdFromSharedPreferences(context: Context): Long {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("user_id", -1)
    }

    fun fabTikla(it: View) {
        Navigation.gecisYap(it, R.id.gorevEkleGecis)
    }

    fun letGecis(it: View) {
        Navigation.gecisYap(it, R.id.letsGecis)
    }
}





