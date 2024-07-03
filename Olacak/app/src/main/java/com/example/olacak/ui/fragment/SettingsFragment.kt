package com.example.olacak.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.olacak.R

import com.example.olacak.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding : FragmentSettingsBinding
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater,container,false)

        sharedPreferences = requireActivity().getSharedPreferences("PomodoroSettings", Context.MODE_PRIVATE)

        val studyDurations = listOf("1 minute", "2 minutes", "3 minutes", "4 minutes", "5 minutes", "6 minutes", "7 minutes", "8 minutes", "9 minutes", "10 minutes", "11 minutes", "12 minutes", "13 minutes", "14 minutes", "15 minutes", "16 minutes", "17 minutes", "18 minutes", "19 minutes", "20 minutes", "21 minutes", "22 minutes", "23 minutes", "24 minutes", "25 minutes", "26 minutes", "27 minutes", "28 minutes", "29 minutes", "30 minutes", "31 minutes", "32 minutes", "33 minutes", "34 minutes", "35 minutes", "36 minutes", "37 minutes", "38 minutes", "39 minutes", "40 minutes", "41 minutes", "42 minutes", "43 minutes", "44 minutes", "45 minutes", "46 minutes", "47 minutes", "48 minutes", "49 minutes", "50 minutes", "51 minutes", "52 minutes", "53 minutes", "54 minutes", "55 minutes", "56 minutes", "57 minutes", "58 minutes", "59 minutes", "60 minutes")
        val breakDurations = listOf("1 minute", "2 minutes", "3 minutes", "4 minutes", "5 minutes", "6 minutes", "7 minutes", "8 minutes", "9 minutes", "10 minutes", "11 minutes", "12 minutes", "13 minutes", "14 minutes", "15 minutes", "16 minutes", "17 minutes", "18 minutes", "19 minutes", "20 minutes", "21 minutes", "22 minutes", "23 minutes", "24 minutes", "25 minutes", "26 minutes", "27 minutes", "28 minutes", "29 minutes", "30 minutes", "31 minutes", "32 minutes", "33 minutes", "34 minutes", "35 minutes", "36 minutes", "37 minutes", "38 minutes", "39 minutes", "40 minutes", "41 minutes", "42 minutes", "43 minutes", "44 minutes", "45 minutes", "46 minutes", "47 minutes", "48 minutes", "49 minutes", "50 minutes", "51 minutes", "52 minutes", "53 minutes", "54 minutes", "55 minutes", "56 minutes", "57 minutes", "58 minutes", "59 minutes", "60 minutes")

        val studyAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, studyDurations)
        val breakAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, breakDurations)

        binding.autoCompleteTextViewStudyTimer.setAdapter(studyAdapter)
        binding.autoCompleteTextViewBreakTimer.setAdapter(breakAdapter)

        val studyAlarmSounds = listOf("Alarm 1", "Alarm 2", "Alarm 3", "Alarm 4", "Alarm 5", "Alarm 6", "Alarm 7", "Alarm 8", "Alarm 9", "Alarm 10", "Alarm 11", "Alarm 12", "Alarm 13", "Alarm 14", "Alarm 15", "Alarm 16", "Alarm 17", "Alarm 18", "Alarm 19", "Alarm 20","Alarm 21","Alarm 22","Alarm 23","Alarm 24","Alarm 25")
        val breakAlarmSounds = listOf("Alarm 1", "Alarm 2", "Alarm 3", "Alarm 4", "Alarm 5", "Alarm 6", "Alarm 7", "Alarm 8", "Alarm 9", "Alarm 10", "Alarm 11", "Alarm 12", "Alarm 13", "Alarm 14", "Alarm 15", "Alarm 16", "Alarm 17", "Alarm 18", "Alarm 19", "Alarm 20","Alarm 21","Alarm 22","Alarm 23","Alarm 24","Alarm 25")

        val studyAlarmAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, studyAlarmSounds)
        val breakAlarmAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, breakAlarmSounds)

        binding.autoCompleteTextViewStudyAlarm.setAdapter(studyAlarmAdapter)
        binding.autoCompleteTextViewBreakAlarm.setAdapter(breakAlarmAdapter)



        binding.buttonSaveSettings.setOnClickListener {
            saveSettings()
            val selectedStudyDuration = binding.autoCompleteTextViewStudyTimer.text.toString()
            val selectedBreakDuration = binding.autoCompleteTextViewBreakTimer.text.toString()
            saveTimerDurations(selectedStudyDuration, selectedBreakDuration)

            val studyAlarmSelection = binding.autoCompleteTextViewStudyAlarm.text.toString()
            val breakAlarmSelection = binding.autoCompleteTextViewBreakAlarm.text.toString()



            Toast.makeText(requireContext(), "Changes are saved!", Toast.LENGTH_SHORT).show()

        }

        return binding.root
    }
    private fun saveTimerDurations(studyDuration: String, breakDuration: String) {
        val studyDurationInMillis = when (studyDuration) {
            "1 minute" -> 1 * 60 * 1000L
            "2 minutes" -> 2 * 60 * 1000L
            "3 minutes" -> 3 * 60 * 1000L
            "4 minutes" -> 4 * 60 * 1000L
            "5 minutes" -> 5 * 60 * 1000L
            "6 minutes" -> 6 * 60 * 1000L
            "7 minutes" -> 7 * 60 * 1000L
            "8 minutes" -> 8 * 60 * 1000L
            "9 minutes" -> 9 * 60 * 1000L
            "10 minutes" -> 10 * 60 * 1000L
            "11 minutes" -> 11 * 60 * 1000L
            "12 minutes" -> 12 * 60 * 1000L
            "13 minutes" -> 13 * 60 * 1000L
            "14 minutes" -> 14 * 60 * 1000L
            "15 minutes" -> 15 * 60 * 1000L
            "16 minutes" -> 16 * 60 * 1000L
            "17 minutes" -> 17 * 60 * 1000L
            "18 minutes" -> 18 * 60 * 1000L
            "19 minutes" -> 19 * 60 * 1000L
            "20 minutes" -> 20 * 60 * 1000L
            "21 minutes" -> 21 * 60 * 1000L
            "22 minutes" -> 22 * 60 * 1000L
            "23 minutes" -> 23 * 60 * 1000L
            "24 minutes" -> 24 * 60 * 1000L
            "25 minutes" -> 25 * 60 * 1000L
            "26 minutes" -> 26 * 60 * 1000L
            "27 minutes" -> 27 * 60 * 1000L
            "28 minutes" -> 28 * 60 * 1000L
            "29 minutes" -> 29 * 60 * 1000L
            "30 minutes" -> 30 * 60 * 1000L
            "31 minutes" -> 31 * 60 * 1000L
            "32 minutes" -> 32 * 60 * 1000L
            "33 minutes" -> 33 * 60 * 1000L
            "34 minutes" -> 34 * 60 * 1000L
            "35 minutes" -> 35 * 60 * 1000L
            "36 minutes" -> 36 * 60 * 1000L
            "37 minutes" -> 37 * 60 * 1000L
            "38 minutes" -> 38 * 60 * 1000L
            "39 minutes" -> 39 * 60 * 1000L
            "40 minutes" -> 40 * 60 * 1000L
            "41 minutes" -> 41 * 60 * 1000L
            "42 minutes" -> 42 * 60 * 1000L
            "43 minutes" -> 43 * 60 * 1000L
            "44 minutes" -> 44 * 60 * 1000L
            "45 minutes" -> 45 * 60 * 1000L
            "46 minutes" -> 46 * 60 * 1000L
            "47 minutes" -> 47 * 60 * 1000L
            "48 minutes" -> 48 * 60 * 1000L
            "49 minutes" -> 49 * 60 * 1000L
            "50 minutes" -> 50 * 60 * 1000L
            "51 minutes" -> 51 * 60 * 1000L
            "52 minutes" -> 52 * 60 * 1000L
            "53 minutes" -> 53 * 60 * 1000L
            "54 minutes" -> 54 * 60 * 1000L
            "55 minutes" -> 55 * 60 * 1000L
            "56 minutes" -> 56 * 60 * 1000L
            "57 minutes" -> 57 * 60 * 1000L
            "58 minutes" -> 58 * 60 * 1000L
            "59 minutes" -> 59 * 60 * 1000L
            "60 minutes" -> 60 * 60 * 1000L
            else -> 1 * 60 * 1000L
        }

        val breakDurationInMillis = when (breakDuration) {
            "1 minute" -> 1 * 60 * 1000L
            "2 minutes" -> 2 * 60 * 1000L
            "3 minutes" -> 3 * 60 * 1000L
            "4 minutes" -> 4 * 60 * 1000L
            "5 minutes" -> 5 * 60 * 1000L
            "6 minutes" -> 6 * 60 * 1000L
            "7 minutes" -> 7 * 60 * 1000L
            "8 minutes" -> 8 * 60 * 1000L
            "9 minutes" -> 9 * 60 * 1000L
            "10 minutes" -> 10 * 60 * 1000L
            "11 minutes" -> 11 * 60 * 1000L
            "12 minutes" -> 12 * 60 * 1000L
            "13 minutes" -> 13 * 60 * 1000L
            "14 minutes" -> 14 * 60 * 1000L
            "15 minutes" -> 15 * 60 * 1000L
            "16 minutes" -> 16 * 60 * 1000L
            "17 minutes" -> 17 * 60 * 1000L
            "18 minutes" -> 18 * 60 * 1000L
            "19 minutes" -> 19 * 60 * 1000L
            "20 minutes" -> 20 * 60 * 1000L
            "21 minutes" -> 21 * 60 * 1000L
            "22 minutes" -> 22 * 60 * 1000L
            "23 minutes" -> 23 * 60 * 1000L
            "24 minutes" -> 24 * 60 * 1000L
            "25 minutes" -> 25 * 60 * 1000L
            "26 minutes" -> 26 * 60 * 1000L
            "27 minutes" -> 27 * 60 * 1000L
            "28 minutes" -> 28 * 60 * 1000L
            "29 minutes" -> 29 * 60 * 1000L
            "30 minutes" -> 30 * 60 * 1000L
            "31 minutes" -> 31 * 60 * 1000L
            "32 minutes" -> 32 * 60 * 1000L
            "33 minutes" -> 33 * 60 * 1000L
            "34 minutes" -> 34 * 60 * 1000L
            "35 minutes" -> 35 * 60 * 1000L
            "36 minutes" -> 36 * 60 * 1000L
            "37 minutes" -> 37 * 60 * 1000L
            "38 minutes" -> 38 * 60 * 1000L
            "39 minutes" -> 39 * 60 * 1000L
            "40 minutes" -> 40 * 60 * 1000L
            "41 minutes" -> 41 * 60 * 1000L
            "42 minutes" -> 42 * 60 * 1000L
            "43 minutes" -> 43 * 60 * 1000L
            "44 minutes" -> 44 * 60 * 1000L
            "45 minutes" -> 45 * 60 * 1000L
            "46 minutes" -> 46 * 60 * 1000L
            "47 minutes" -> 47 * 60 * 1000L
            "48 minutes" -> 48 * 60 * 1000L
            "49 minutes" -> 49 * 60 * 1000L
            "50 minutes" -> 50 * 60 * 1000L
            "51 minutes" -> 51 * 60 * 1000L
            "52 minutes" -> 52 * 60 * 1000L
            "53 minutes" -> 53 * 60 * 1000L
            "54 minutes" -> 54 * 60 * 1000L
            "55 minutes" -> 55 * 60 * 1000L
            "56 minutes" -> 56 * 60 * 1000L
            "57 minutes" -> 57 * 60 * 1000L
            "58 minutes" -> 58 * 60 * 1000L
            "59 minutes" -> 59 * 60 * 1000L
            "60 minutes" -> 60 * 60 * 1000L
            else -> 1 * 60 * 1000L
        }

        val sharedPreferences = requireActivity().getSharedPreferences("PomodoroSettings", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong("study_timer", studyDurationInMillis)
        editor.putLong("break_timer", breakDurationInMillis)
        editor.apply()
    }

    private fun savePreferences(studyTimer: String, breakTimer: String,studyAlarm: String,breakAlarm:String) {
        with(sharedPreferences.edit()) {
            putString("study_timer", studyTimer)
            putString("break_timer", breakTimer)
            putString("study_alarm", studyAlarm)
            putString("break_alarm", breakAlarm)
            apply()
        }
    }




    private fun saveSettings() {
        val editor = sharedPreferences.edit()

        editor.putString("studyTimer", binding.autoCompleteTextViewStudyTimer.text.toString())
        editor.putString("breakTimer", binding.autoCompleteTextViewBreakTimer.text.toString())

        val selectedTheme = if (binding.radioButtonDark.isChecked) "Dark" else "Light"
        editor.putString("theme", selectedTheme)

        editor.putString("studyAlarm", binding.autoCompleteTextViewStudyAlarm.text.toString())
        editor.putString("breakAlarm", binding.autoCompleteTextViewBreakAlarm.text.toString())

        editor.apply()


        requireActivity().recreate()
    }
}








