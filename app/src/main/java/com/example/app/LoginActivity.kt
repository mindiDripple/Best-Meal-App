package com.example.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Navigate to Home on login (no backend yet)
        findViewById<android.view.View>(R.id.btn_login).setOnClickListener {
            // In a real app validate inputs then authenticate
            getSharedPreferences("bestmeal_prefs", MODE_PRIVATE)
                .edit().putBoolean("logged_in", true).apply()
            val intent = Intent(this, SplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        // Navigate to Signup
        findViewById<android.view.View>(R.id.tv_go_signup).setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
