package com.example.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)

        // Navigate to Home on signup (no backend yet)
        findViewById<android.view.View>(R.id.btn_signup).setOnClickListener {
            getSharedPreferences("bestmeal_prefs", MODE_PRIVATE)
                .edit().putBoolean("logged_in", true).apply()
            val intent = Intent(this, SplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        // Navigate to Login
        findViewById<android.view.View>(R.id.tv_go_login).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
