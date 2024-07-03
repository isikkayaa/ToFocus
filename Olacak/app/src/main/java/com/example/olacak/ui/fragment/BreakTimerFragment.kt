package com.example.olacak.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.olacak.R
import com.example.olacak.databinding.FragmentBreakTimerBinding
import com.example.olacak.ui.viewmodel.BreakTimerViewModel
import com.example.olacak.util.Constants
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BreakTimerFragment : Fragment() {
    private lateinit var binding: FragmentBreakTimerBinding
    private val viewModel: BreakTimerViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_break_timer, container, false)


        binding.buttonFinishBreak.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_breakTimerFragment_to_studyTimerFragment)
        }

        binding.buttonGame.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_breakTimerFragment_to_gamesActivity)
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonStopResumeBreak.text = getString(R.string.stop_break)
        binding.timerBreak.text = getString(R.string.initial_value)

        val sharedPreferences = requireContext().getSharedPreferences("PomodoroSettings", Context.MODE_PRIVATE)
        val breakTimeStr = sharedPreferences.getString("breakTimer", "1 minute") ?: "1 minute"
        val breakTime = convertToMilliseconds(breakTimeStr)

        viewModel.startBreakTimer(breakTime)

        observeBreakTimer()

        binding.buttonStopResumeBreak.setOnClickListener {
            when (binding.buttonStopResumeBreak.text) {
                getString(R.string.stop_break) -> {
                    viewModel.stopBreakTimer()
                    binding.buttonStopResumeBreak.text = getString(R.string.resume_break)
                }

                getString(R.string.resume_break) -> {
                    viewModel.resumeBreakTimer()
                    binding.buttonStopResumeBreak.text = getString(R.string.stop_break)
                }
            }
        }
    }

    private fun observeBreakTimer() {
        viewModel.breakTimerLiveData.observe(viewLifecycleOwner) { timeText ->
            binding.timerBreak.text = timeText
        }

        viewModel.breakTimerFinished.observe(viewLifecycleOwner) {
            if (it == true) {
                Toast.makeText(context, "Break finished!", Toast.LENGTH_SHORT).show()

                Navigation.findNavController(binding.root).navigate(R.id.action_breakTimerFragment_to_studyTimerFragment)
            }
        }
    }

    private fun convertToMilliseconds(timeStr: String): Long {
        return when (timeStr) {
            "1 minute" -> 1 * 60 * 1000L
            "2 minutes" -> 2 * 60 * 1000L
            "3 minutes" -> 3 * 60 * 1000L
            else -> 1 * 60 * 1000L
        }
    }
}

