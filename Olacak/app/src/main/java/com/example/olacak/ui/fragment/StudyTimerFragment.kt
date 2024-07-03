package com.example.olacak.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.olacak.R
import com.example.olacak.data.entity.Pomodoro
import com.example.olacak.data.repo.GorevlerRepository
import com.example.olacak.databinding.FragmentStudyTimerBinding
import com.example.olacak.room.GorevlerDao
import com.example.olacak.room.PomodoroDao
import com.example.olacak.room.UserTaskDao
import com.example.olacak.room.Veritabani
import com.example.olacak.ui.viewmodel.StudyTimerViewModel
import com.example.olacak.util.Constants
import com.example.olacak.util.makeToast
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class StudyTimerFragment : Fragment() {
    private lateinit var binding: FragmentStudyTimerBinding
    private val viewModel: StudyTimerViewModel by viewModels()
    private lateinit var gorevlerDao: GorevlerDao
    private lateinit var userTaskDao: UserTaskDao
    private lateinit var pomodoroDao: PomodoroDao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_study_timer, container, false)
        binding.studyTimerFragment = this

        setupAutoCompleteTextView()
        val database = Veritabani.getDatabase(requireContext())
        gorevlerDao = database.gorevlerDao()
        userTaskDao = database.userTaskDao()





        binding.buttonFinish.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_studyTimerFragment_to_breakTimerFragment)
        }

        return binding.root
    }

    private fun setupAutoCompleteTextView() {
        viewModel.taskNames.observe(viewLifecycleOwner, Observer { taskNames ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, taskNames)
            binding.autoCompleteTextView5.setAdapter(adapter)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonStart.text = getString(R.string.start_string)
        binding.timerStudyTimer.text = getString(R.string.initial_value)

        pomodoroStartStopClickListener()
        pomodoroResetClickListener()

        observeTimerFinished()


    }

    override fun onResume() {
        super.onResume()

    }

    private fun autoCompleteTextNullCheck(): Boolean {
        return if (binding.autoCompleteTextView5.text.isNullOrBlank()) {
            Toast.makeText(context, "You have to choose task name", Toast.LENGTH_SHORT).show()
            true
        } else {
            false
        }
    }

    private fun pomodoroStartStopClickListener() {
        binding.buttonStart.setOnClickListener {
            if (autoCompleteTextNullCheck()) {
                return@setOnClickListener
            }

            when (binding.buttonStart.text) {
                getString(R.string.start_string) -> {
                    Log.d("Pomodoro", "Starting work timer")
                    viewModel.startTimerForWork()
                    viewModel.pomodoroTextLiveData.observe(viewLifecycleOwner) { timeText ->
                        binding.timerStudyTimer.text = timeText
                    }
                    binding.buttonStart.text = getString(R.string.stop_string)
                }

                getString(R.string.stop_string) -> {
                    Log.d("Pomodoro", "Stopping timer")
                    viewModel.stopCountDownTimer()
                    binding.buttonStart.text = getString(R.string.resume_string)
                }

                getString(R.string.resume_string) -> {
                    Log.d("Pomodoro", "Resuming timer")
                    viewModel.resumeCountDownTimer()
                    binding.buttonStart.text = getString(R.string.stop_string)
                }
            }
        }
    }

    private fun pomodoroResetClickListener() {
        binding.buttonReset.setOnClickListener {
            if (autoCompleteTextNullCheck()) {
                return@setOnClickListener
            }
            Log.d("Pomodoro", "Resetting timer")
            viewModel.resetCountDownTimer()
            binding.buttonStart.text = getString(R.string.start_string)
            binding.timerStudyTimer.text = getString(R.string.initial_value)
        }
    }

    private fun observeTimerFinished() {
        viewModel.workTimerFinished.observe(viewLifecycleOwner) { isFinished ->
            lifecycleScope.launch {
                if (isFinished == true) {
                    val userId = viewModel.getUserIdFromSharedPreferences()
                    val pomodoro = Pomodoro(
                        taskName = binding.autoCompleteTextView5.text.toString(),
                        createdAt = LocalDateTime.now(),
                        workTime = viewModel.getUserWorkTime(),
                        userId = userId
                    )
                    viewModel.insertAndUpdatePomodoro(pomodoro)

                    binding.buttonStart.text = getString(R.string.start_string)

                    delay(3000L)
                    viewModel.startTimerForWork()
                    Navigation.findNavController(binding.root).navigate(R.id.action_studyTimerFragment_to_breakTimerFragment)
                }





            }

            }
        }
}














