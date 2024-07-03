package com.example.olacak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.olacak.databinding.ActivityHangmanBinding
import kotlin.random.Random

class HangmanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHangmanBinding
    private var falseCount=0
    private var gameOverFlag=true
    private lateinit var word:String
    private lateinit var targetWord:String
    private lateinit var indexes:MutableList<Int>
    private var randomNumber=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHangmanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startGame()
        for(letter in 'a'..'z'){
            val buttonId=resources.getIdentifier(letter.toString(),"id",packageName)
            val button=findViewById<View>(buttonId)
            button.setOnClickListener {
                indexes=findIndexes(binding, word, letter)
                targetWord=displayLetters(indexes, targetWord, letter)
                button.visibility=View.GONE

            }
        }

    }

    private fun startGame() {
        callBackButtons()
        falseCount=0
        binding.hangman.setImageResource(0)
        randomNumber=Random.nextInt(0,320)
        word=Words.DICTIONARY[randomNumber]
        createBlanks(word.length,binding)
        targetWord=binding.word.text.toString()

    }

    private fun callBackButtons() {
        for(letter in 'a'..'z'){
            val buttonId=resources.getIdentifier(letter.toString(),"id",packageName)
            val button=findViewById<View>(buttonId)
            button.visibility=View.VISIBLE
        }
    }

    private fun createBlanks(size:Int,binding: ActivityHangmanBinding) {
        binding.word.text="_ ".repeat(size)
    }

    private fun findIndexes(binding: ActivityHangmanBinding,word:String,letter:Char):MutableList<Int>{
        val indexes= mutableListOf<Int>()

        word.mapIndexed { index, char ->
            if (char == letter) {
                indexes.add(index)
            }
        }
        if(indexes.size==0){
            if(falseCount==10){
                gameOverFlag=false
                showGameOverDialog(gameOverFlag)
            }
            falseCount++
            updateImage(binding,falseCount)
        }

        return indexes
    }

    private fun updateImage(binding: ActivityHangmanBinding, falseCount: Int) {
        val imageName="hangman_$falseCount"
        val imageResourceId=resources.getIdentifier(imageName,"drawable",packageName)
        binding.hangman.setImageResource(imageResourceId)

    }


    private fun displayLetters(indexes:MutableList<Int>,targetWord:String,letter: Char):String{
        val stringBuilder=StringBuilder(targetWord)
        if(indexes.size>0){
            indexes.map {index->
                stringBuilder.setCharAt(index*2,letter.uppercaseChar())
                binding.word.text=stringBuilder.toString()
            }
        }

        if(!stringBuilder.contains("_")){
            gameOverFlag=true
            showGameOverDialog(gameOverFlag)
        }

        return stringBuilder.toString()

    }

    private fun showGameOverDialog(gameOverFlag: Boolean) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)

        if (gameOverFlag) {
            builder.setTitle("YOU WON")
            builder.setMessage("Congrats! You Won The Game")

            builder.setPositiveButton("Play Again") { dialog, which ->
                startGame()
            }
            builder.setNegativeButton("Exit") { dialog, which ->
                System.exit(0)
            }

        }

        else{
            builder.setTitle("GAME OVER")
            builder.setMessage("You Lost The Game. The word was ${word.uppercase()}")

            builder.setPositiveButton("Play Again") { dialog, which ->
                startGame()
            }
            builder.setNegativeButton("Exit") { dialog, which ->
                System.exit(0)
            }

        }

        builder.show()

    }
}