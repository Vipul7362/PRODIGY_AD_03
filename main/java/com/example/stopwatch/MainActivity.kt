package com.example.stopwatch



import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvTime: TextView
    private lateinit var btnStart: Button
    private lateinit var btnPause: Button
    private lateinit var btnReset: Button

    private var handler = Handler()
    private var startTime = 0L
    private var timeInMillis = 0L
    private var timeSwapBuff = 0L
    private var updateTime = 0L

    private val updateTimerThread: Runnable = object : Runnable {
        override fun run() {
            timeInMillis = SystemClock.uptimeMillis() - startTime
            updateTime = timeSwapBuff + timeInMillis

            val secs = (updateTime / 1000).toInt()
            val mins = secs / 60
            val milliseconds = (updateTime % 1000).toInt()

            tvTime.text = String.format("%02d:%02d:%03d", mins, secs % 60, milliseconds)
            handler.postDelayed(this, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTime = findViewById(R.id.tvTime)
        btnStart = findViewById(R.id.btnStart)
        btnPause = findViewById(R.id.btnPause)
        btnReset = findViewById(R.id.btnReset)

        btnStart.setOnClickListener {
            startTime = SystemClock.uptimeMillis()
            handler.postDelayed(updateTimerThread, 0)
        }

        btnPause.setOnClickListener {
            timeSwapBuff += timeInMillis
            handler.removeCallbacks(updateTimerThread)
        }

        btnReset.setOnClickListener {
            startTime = 0L
            timeInMillis = 0L
            timeSwapBuff = 0L
            updateTime = 0L
            handler.removeCallbacks(updateTimerThread)
            tvTime.text = "00:00:000"
        }
    }
}
