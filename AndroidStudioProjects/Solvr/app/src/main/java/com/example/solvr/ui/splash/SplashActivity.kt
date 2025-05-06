package com.example.solvr.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.solvr.ui.user.UserActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Delay diubah dari 30000 menjadi 2000 (2 detik)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}