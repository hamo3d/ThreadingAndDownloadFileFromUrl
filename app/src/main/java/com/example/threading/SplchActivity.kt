package com.example.threading

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.threading.databinding.ActivitySplchBinding

class SplchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplchBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    override fun onStart() {
        super.onStart()
        handleSplashActivity()
    }


    private fun handleSplashActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(applicationContext, AsyncTaskClass::class.java))
            finish()
        }, 3000)
    }
}