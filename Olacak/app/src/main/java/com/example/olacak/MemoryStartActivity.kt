package com.example.olacak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.olacak.databinding.ActivityMemoryStartBinding

class MemoryStartActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMemoryStartBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoryStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnStart.setOnClickListener {
            startActivity(Intent(this@MemoryStartActivity,MemoryActivity::class.java))
        }


    }
}

