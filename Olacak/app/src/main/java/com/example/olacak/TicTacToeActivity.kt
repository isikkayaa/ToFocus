package com.example.olacak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.olacak.databinding.ActivityMainBinding
import com.example.olacak.databinding.ActivityTicTacToeBinding
import java.util.Random

import android.graphics.Typeface
import android.os.Handler
import android.os.Looper


class TicTacToeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTicTacToeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicTacToeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun buClick(view: View) {
        val buSelected = view as Button
        var cellId = 0
        when (buSelected.id) {
            R.id.bu1 -> cellId = 1
            R.id.bu2 -> cellId = 2
            R.id.bu3 -> cellId = 3
            R.id.bu4 -> cellId = 4
            R.id.bu5 -> cellId = 5
            R.id.bu6 -> cellId = 6
            R.id.bu7 -> cellId = 7
            R.id.bu8 -> cellId = 8
            R.id.bu9 -> cellId = 9
        }
        playGame(cellId, buSelected)
    }

    var activePlayer = 1
    var player1 = ArrayList<Int>()
    var player2 = ArrayList<Int>()

    fun playGame(cellId: Int, buSelected: Button) {
        if (activePlayer == 1) {
            buSelected.text = "X"
            buSelected.setTypeface(null, Typeface.BOLD)
            buSelected.textSize = 24F
            player1.add(cellId)
            activePlayer = 2
        } else {
            buSelected.text = "O"
            buSelected.setTypeface(null, Typeface.BOLD)
            buSelected.textSize = 24F
            player2.add(cellId)
            activePlayer = 1
        }
        buSelected.isEnabled = false
        checkWinner()
    }

    fun checkWinner() {
        var winner = -1


        if (player1.containsAll(listOf(1, 2, 3)) || player1.containsAll(listOf(4, 5, 6)) || player1.containsAll(listOf(7, 8, 9))) winner = 1
        if (player2.containsAll(listOf(1, 2, 3)) || player2.containsAll(listOf(4, 5, 6)) || player2.containsAll(listOf(7, 8, 9))) winner = 2


        if (player1.containsAll(listOf(1, 4, 7)) || player1.containsAll(listOf(2, 5, 8)) || player1.containsAll(listOf(3, 6, 9))) winner = 1
        if (player2.containsAll(listOf(1, 4, 7)) || player2.containsAll(listOf(2, 5, 8)) || player2.containsAll(listOf(3, 6, 9))) winner = 2


        if (player1.containsAll(listOf(1, 5, 9)) || player1.containsAll(listOf(3, 5, 7))) winner = 1
        if (player2.containsAll(listOf(1, 5, 9)) || player2.containsAll(listOf(3, 5, 7))) winner = 2

        if (winner != -1) {
            if (winner == 1) {
                player1WinsCounts++
                Toast.makeText(this, "Player 1 wins the game", Toast.LENGTH_LONG).show()
            } else {
                player2WinsCounts++
                Toast.makeText(this, "Player 2 wins the game", Toast.LENGTH_LONG).show()
            }
            Handler(Looper.getMainLooper()).postDelayed({ restartGame() }, 2000)
        } else {
            if (player1.size + player2.size == 9) {
                Toast.makeText(this, "It's a draw!", Toast.LENGTH_LONG).show()
                Handler(Looper.getMainLooper()).postDelayed({ restartGame() }, 2000)
            } else {
                if (activePlayer == 2) {
                    autoPlay()
                }
            }
        }
    }

    fun autoPlay() {
        val emptyCells = (1..9).filter { it !in player1 && it !in player2 }
        if (emptyCells.isNotEmpty()) {
            val cellId = emptyCells.random()
            val buSelected: Button? = when (cellId) {
                1 -> binding.bu1
                2 -> binding.bu2
                3 -> binding.bu3
                4 -> binding.bu4
                5 -> binding.bu5
                6 -> binding.bu6
                7 -> binding.bu7
                8 -> binding.bu8
                9 -> binding.bu9
                else -> null
            }
            buSelected?.let { playGame(cellId, it) }
        }
    }

    var player1WinsCounts = 0
    var player2WinsCounts = 0

    fun restartGame() {
        activePlayer = 1
        player1.clear()
        player2.clear()
        for (cellId in 1..9) {
            val buSelected: Button? = when (cellId) {
                1 -> binding.bu1
                2 -> binding.bu2
                3 -> binding.bu3
                4 -> binding.bu4
                5 -> binding.bu5
                6 -> binding.bu6
                7 -> binding.bu7
                8 -> binding.bu8
                9 -> binding.bu9
                else -> null
            }
            buSelected?.let {
                it.text = ""
                it.setBackgroundColor(resources.getColor(R.color.buttonDefault, null))
                it.isEnabled = true
            }
        }
        Toast.makeText(this, "Player 1: $player1WinsCounts, Player 2: $player2WinsCounts", Toast.LENGTH_LONG).show()
        }
}



