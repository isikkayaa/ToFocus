package com.example.olacak.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.olacak.R
import com.example.olacak.data.entity.Gorevler
import com.example.olacak.data.entity.UserTask
import com.example.olacak.databinding.FragmentGorevEklemeBinding
import com.example.olacak.ui.viewmodel.GorevEklemeViewModel
import com.example.olacak.util.gecisYap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GorevEklemeFragment : Fragment() {
    private lateinit var binding: FragmentGorevEklemeBinding
    private lateinit var viewModel: GorevEklemeViewModel
    private var userId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(GorevEklemeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gorev_ekleme, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = getUserIdFromSharedPreferences()
        val userNesnesi = Gorevler(gorev_id = 0, gorev_adi = "", gorev_aciklamasi = "", user_id = userId)
        binding.userNesnesi = userNesnesi

        binding.buttonKaydet.setOnClickListener {
            lifecycleScope.launch {
                viewModel.kaydet(
                    gorev_adi = binding.editTextTaskAdiSave.text.toString(),
                    gorev_aciklamasi = binding.editTextTaskAciklamaSave.text.toString(),
                    user_id = userId
                )
                findNavController().navigateUp()
            }
        }
    }

    private fun getUserIdFromSharedPreferences(): Long {
        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("user_id", -1)
    }
}




