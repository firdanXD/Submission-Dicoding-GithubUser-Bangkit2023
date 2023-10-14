package com.dicoding.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.githubuser.R

class SplashActivity : AppCompatActivity() {

    private val splashDelay: Long = 2000
    private val typingText = "Welcome To Github"

    private lateinit var tvTyping: TextView
    private var currentIndex = 0
    private val typingDelay = 100L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        tvTyping = findViewById(R.id.tv_typing)
        startTypingAnimation()

        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, splashDelay)
    }

    private fun startTypingAnimation() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (currentIndex < typingText.length) {
                    val currentText = typingText.substring(0, currentIndex + 1)
                    tvTyping.text = currentText
                    currentIndex++
                    handler.postDelayed(this, typingDelay)
                }
            }
        }, typingDelay)
    }
}
