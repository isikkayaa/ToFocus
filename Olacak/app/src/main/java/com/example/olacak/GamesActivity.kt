package com.example.olacak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.navigation.Navigation
import com.example.olacak.databinding.ActivityGamesBinding

class GamesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGamesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val button = findViewById<Button>(R.id.buttonSnakeGame)
        val button1= findViewById<Button>(R.id.buttonHangman)
        val button2 = findViewById<Button>(R.id.buttonMemoryGame)
        val button3 = findViewById<Button>(R.id.buttonMathGame)
        val button4 = findViewById<Button>(R.id.buttonBrickGame)
        val button5 = findViewById<Button>(R.id.buttonTicTacToeGame)


        button.setOnClickListener {
            val intent = Intent(this,SnakeGameActivity::class.java)

            startActivity(intent)
        }

        button1.setOnClickListener {
            val intent = Intent(this,HangmanActivity::class.java)

            startActivity(intent)
        }

        button2.setOnClickListener {
            val intent = Intent(this,MemoryStartActivity::class.java)

            startActivity(intent)
        }



        button3.setOnClickListener {
            val intent = Intent(this,MathGameActivity::class.java)

            startActivity(intent)
        }

        button4.setOnClickListener {
            val intent = Intent(this,BrickBrakerActivity::class.java)

            startActivity(intent)
        }


        button5.setOnClickListener {
            val intent = Intent(this,TicTacToeActivity::class.java)

            startActivity(intent)
        }







    }
}