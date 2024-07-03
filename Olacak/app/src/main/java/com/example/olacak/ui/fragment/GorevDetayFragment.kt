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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.olacak.R
import com.example.olacak.data.entity.UserTask
import com.example.olacak.databinding.FragmentGorevDetayBinding
import com.example.olacak.ui.viewmodel.GorevDetayViewModel
import com.example.olacak.ui.viewmodel.GorevEklemeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GorevDetayFragment : Fragment() {
    private lateinit var binding: FragmentGorevDetayBinding
    private lateinit var viewModel: GorevDetayViewModel
    private var userId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(GorevDetayViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gorev_detay, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle:GorevDetayFragmentArgs by navArgs()
        val gelenGorev = bundle.gorev
        binding.gorevNesnesi = gelenGorev

        userId = getUserIdFromSharedPreferences()


        binding.buttonGuncelle.setOnClickListener {
            lifecycleScope.launch {
                viewModel.guncelle(
                    gorev_id = gelenGorev.gorev_id,
                    gorev_adi = binding.editTextTaskAdi.text.toString(),
                    gorev_aciklamasi = binding.editTextTaskAciklama.text.toString(),
                    user_id = gelenGorev.user_id
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
