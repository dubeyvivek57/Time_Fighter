package com.example.timefighter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    internal var score: Int = 0
    internal var gameStarted = false
    internal lateinit var countDownTimer: CountDownTimer
    internal val initialCountDown: Long = 20000
    internal val countDownIntervel: Long = 1000
    internal var timeLeftOnTimer : Long = 20000
    internal lateinit var yourScoreTextView: TextView
    internal lateinit var timerTextView: TextView
    internal lateinit var tapMeButton: Button

    companion object{
        private val TAG = MainActivity::class.java.simpleName
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate called. Score is : $score")

        yourScoreTextView = findViewById(R.id.yourScoreTextView)
        timerTextView = findViewById(R.id.timeLeftTextView)
        tapMeButton = findViewById(R.id.tapMeButton)

        tapMeButton.setOnClickListener { view ->
            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
            view.startAnimation(bounceAnimation)
            incrementScore()
        }
        if (savedInstanceState!=null){
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        }else{
            resetGame()
        }
    }

    private fun restoreGame() {
        yourScoreTextView.text = getString(R.string.yourScoreTextView, score)
        val restoredTime = timeLeftOnTimer/1000
        timerTextView.text = getString(R.string.timeLeftTextView, restoredTime)

        countDownTimer = object : CountDownTimer(timeLeftOnTimer, countDownIntervel){
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished/1000
                timerTextView.text = getString(R.string.timeLeftTextView, timeLeft)
            }

            override fun onFinish() {
                endGame()
            }
        }
        countDownTimer.start()
        gameStarted = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
        countDownTimer.cancel()

        Log.d(TAG, "onSaveInstanceState: Saveing score : $score and Time Left : $timeLeftOnTimer")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy called.")
    }
    private fun resetGame() {
        score = 0
        yourScoreTextView.text = getString(R.string.timeLeftTextView, score)

        val initialTimeLeft = initialCountDown / 1000
        timerTextView.text = getString(R.string.timeLeftTextView, initialTimeLeft)

        countDownTimer = object : CountDownTimer(initialCountDown, countDownIntervel) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
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

        val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink)
        yourScoreTextView.startAnimation(blinkAnimation)
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
