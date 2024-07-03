package com.example.olacak

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.olacak.databinding.ActivityMainBinding
import com.example.olacak.databinding.ActivityMathGameBinding
import com.example.olacak.databinding.DialogResultBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlin.random.Random

class MathGameActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMathGameBinding
    private var isPlayed = false
    private var firstRandomNumber : Int ?= null
    private var secondRandomNumber : Int ?= null

    @SuppressLint("SetTextI18n")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMathGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            btnStartOrNext.setOnClickListener {

                if (isPlayed)
                {

                    getRandomNumbers()
                    tvScore.text = (tvScore.text.toString().toInt()-1).toString()
                }else
                {

                    isPlayed = true
                    btnStartOrNext.text = "Next!"
                    cardQuestion.visibility = View.VISIBLE
                    cardScore.visibility = View.VISIBLE
                    getRandomNumbers()
                    runTimer()


                }

            }
            etAnswer.addTextChangedListener {
                val answer = firstRandomNumber!! + secondRandomNumber!!
                if (!it.isNullOrEmpty() && it.toString().toInt() == answer)
                {
                    //answer is true
                    tvScore.text = (tvScore.text.toString().toInt()+1).toString()
                    etAnswer.setText("")
                    getRandomNumbers()
                }
            }
        }
    }


    private fun runTimer() {
        lifecycleScope.launch(Dispatchers.IO)
        {
            (1..29).asFlow().onStart {

                binding.constraintLayout.transitionToEnd()

            }.onCompletion {


                runOnUiThread {
                    binding.cardQuestion.visibility = View.GONE
                    val dialogBinding = DialogResultBinding.inflate(layoutInflater)
                    val dialog = Dialog(this@MathGameActivity)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setContentView(dialogBinding.root)
                    dialog.setCancelable(false)
                    dialog.show()
                    dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT)
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


                    dialogBinding.apply {

                        tvDialogScore.text = binding.tvScore.text
                        btnClose.setOnClickListener {
                            dialog.dismiss()
                            finish()


                        }
                        btnTryAgain.setOnClickListener {
                            dialog.dismiss()
                            binding.apply {
                                btnStartOrNext.text = getString(R.string.start_game)
                                cardQuestion.visibility = View.GONE
                                cardScore.visibility = View.GONE
                                isPlayed = false
                                constraintLayout.setTransition(R.id.start,R.id.end)
                                constraintLayout.transitionToEnd()
                                tvScore.text = "0"
                            }
                        }
                    }


                }

            }.collect{ delay(1000) }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun getRandomNumbers()
    {
        firstRandomNumber = Random.nextInt(2,99)
        secondRandomNumber = Random.nextInt(2,99)
        binding.tvQuestionNumber.text = "$firstRandomNumber + $secondRandomNumber"
    }
}