package com.example.olacak.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.example.olacak.databinding.FragmentGameSelectionBinding


class GameSelectionFragment : Fragment() {

    private lateinit var binding: FragmentGameSelectionBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGameSelectionBinding.inflate(inflater,container,false)




        return binding.root
        }











}



