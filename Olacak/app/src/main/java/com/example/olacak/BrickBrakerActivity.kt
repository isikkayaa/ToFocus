package com.example.olacak


import android.animation.ValueAnimator
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast



class BrickBrakerActivity : AppCompatActivity() {

    private lateinit var scoreText: TextView
    private lateinit var paddle: View
    private lateinit var ball: View
    private lateinit var brickContainer: LinearLayout

    private var ballX = 0f
    private var ballY = 0f
    private var ballSpeedX = 0f
    private var ballSpeedY = 0f
    private var paddleX = 0f
    private var score = 0
    private var lives = 3

    private val brickRows = 9
    private val brickColumns = 10
    private val brickWidth = 100
    private val brickHeight = 40
    private val brickMargin = 4

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brick_braker)

        scoreText = findViewById(R.id.scoreText)
        paddle = findViewById(R.id.paddle)
        ball = findViewById(R.id.ball)
        brickContainer = findViewById(R.id.brickContainer)

        val newgame = findViewById<Button>(R.id.newgame)
        newgame.setOnClickListener {
            initializeBricks()
            start()
            newgame.visibility = View.INVISIBLE
        }
    }

    private fun initializeBricks() {
        brickContainer.removeAllViews()
        for (row in 0 until brickRows) {
            val rowLayout = LinearLayout(this)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            rowLayout.layoutParams = params

            for (col in 0 until brickColumns) {
                val brick = View(this)
                val brickParams = LinearLayout.LayoutParams(brickWidth, brickHeight)
                brickParams.setMargins(brickMargin, brickMargin, brickMargin, brickMargin)
                brick.layoutParams = brickParams
                brick.setBackgroundColor(resources.getColor(R.color.brick_color))
                rowLayout.addView(brick)
            }
            brickContainer.addView(rowLayout)
        }
    }

    private fun moveBall() {
        ballX += ballSpeedX
        ballY += ballSpeedY

        ball.x = ballX
        ball.y = ballY
    }

    private fun movePaddle(x: Float) {
        paddleX = x - paddle.width / 2
        paddle.x = paddleX
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun checkCollision() {
        val screenWidth = resources.displayMetrics.widthPixels.toFloat()
        val screenHeight = resources.displayMetrics.heightPixels.toFloat()

        if (ballX <= 0 || ballX + ball.width >= screenWidth) {
            ballSpeedX *= -1
        }

        if (ballY <= 0) {
            ballSpeedY *= -1
        }

        if (ballY + ball.height >= paddle.y && ballY + ball.height <= paddle.y + paddle.height
            && ballX + ball.width >= paddle.x && ballX <= paddle.x + paddle.width
        ) {
            ballSpeedY *= -1
            score++
            scoreText.text = "Score: $score"
        }

        if (ballY + ball.height >= screenHeight) {
            resetBallPosition()
        }

        for (row in 0 until brickRows) {
            val rowLayout = brickContainer.getChildAt(row) as LinearLayout
            val rowTop = rowLayout.y + brickContainer.y
            val rowBottom = rowTop + rowLayout.height

            for (col in 0 until brickColumns) {
                val brick = rowLayout.getChildAt(col) as View

                if (brick.visibility == View.VISIBLE) {
                    val brickLeft = brick.x + rowLayout.x
                    val brickRight = brickLeft + brick.width
                    val brickTop = brick.y + rowTop
                    val brickBottom = brickTop + brick.height

                    if (ballX + ball.width >= brickLeft && ballX <= brickRight
                        && ballY + ball.height >= brickTop && ballY <= brickBottom
                    ) {
                        brick.visibility = View.INVISIBLE
                        ballSpeedY *= -1
                        score++
                        scoreText.text = "Score: $score"
                        return
                    }
                }
            }
        }

        if (ballY + ball.height >= screenHeight - 100) {
            lives--

            if (lives > 0) {
                Toast.makeText(this, "$lives balls left ", Toast.LENGTH_SHORT).show()
            }

            if (lives <= 0) {
                gameOver()
            } else {
                resetBallPosition()
                start()
            }
        }
    }

    private fun resetBallPosition() {
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels.toFloat()
        val screenHeight = displayMetrics.heightPixels.toFloat()

        ballX = screenWidth / 2 - ball.width / 2
        ballY = screenHeight / 2 - ball.height / 2  // +525'yi kaldırdık

        ball.x = ballX
        ball.y = ballY

        ballSpeedX = 0f
        ballSpeedY = 0f

        paddleX = screenWidth / 2 - paddle.width / 2
        paddle.x = paddleX
    }

    private fun gameOver() {
        scoreText.text = "Game Over"
        score = 0
        val newgame = findViewById<Button>(R.id.newgame)
        newgame.visibility = View.VISIBLE
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun movepaddle() {
        paddle.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    movePaddle(event.rawX)
                }
            }
            true
        }
    }

    private fun start() {
        movepaddle()
        val displayMetrics = resources.displayMetrics
        val screenDensity = displayMetrics.density

        val screenWidth = displayMetrics.widthPixels.toFloat()
        val screenHeight = displayMetrics.heightPixels.toFloat()

        paddleX = screenWidth / 2 - paddle.width / 2
        paddle.x = paddleX

        ballX = screenWidth / 2 - ball.width / 2
        ballY = screenHeight / 2 - ball.height / 2

        ballSpeedX = 1 * screenDensity  // Hızları küçülttük
        ballSpeedY = -1 * screenDensity

        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = Long.MAX_VALUE
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animation ->
            moveBall()
            checkCollision()
        }
        animator.start()
    }
}

