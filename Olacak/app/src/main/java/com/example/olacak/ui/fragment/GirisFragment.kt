package com.example.olacak.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.olacak.R
import com.example.olacak.databinding.FragmentGirisBinding


class GirisFragment : Fragment() {
    private lateinit var binding: FragmentGirisBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGirisBinding.inflate(inflater,container,false)

        binding.buttonCreate.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_girisFragment_to_createAccountFragment)
        }

        binding.buttonLogin.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_girisFragment_to_loginFragment)
        }


        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}