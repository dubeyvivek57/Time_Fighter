package com.example.timefighter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    internal var score: Int = 0
    internal var gameStarted = false
    internal lateinit var countDownTimer: CountDownTimer
    internal val initialCountDown: Long = 20000
    internal val countDownIntervel: Long = 1000
    internal lateinit var yourScoreTextView: TextView
    internal lateinit var timerTextView: TextView
    internal lateinit var tapMeButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        yourScoreTextView = findViewById(R.id.yourScoreTextView)
        timerTextView = findViewById(R.id.timeLeftTextView)
        tapMeButton = findViewById(R.id.tapMeButton)

        tapMeButton.setOnClickListener { view ->
            incrementScore()
        }
        resetGame()
    }

    private fun resetGame() {
        score = 0
        yourScoreTextView.text = getString(R.string.timeLeftTextView, score)

        val initialTimeLeft = initialCountDown / 1000
        timerTextView.text = getString(R.string.timeLeftTextView, initialTimeLeft)

        countDownTimer = object : CountDownTimer(initialCountDown, countDownIntervel) {
            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = millisUntilFinished / 1000
                timerTextView.text = getString(R.string.timeLeftTextView, timeLeft)
            }

            override fun onFinish() {
                endGame()
                //To be implemented Later
            }
        }
        gameStarted = false
    }

    private fun incrementScore() {
        if (!gameStarted) {
            startTimer()
        }
        score += 1
        val updatedScore = getString(R.string.yourScoreTextView, score)
        yourScoreTextView.text = updatedScore
    }

    private fun startTimer() {
        countDownTimer.start()
        gameStarted = true
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.gameOverMessage, score), Toast.LENGTH_SHORT)
            .show()
        resetGame()
    }
}
