package com.example.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Back arrow returns to previous screen
        findViewById<android.view.View?>(R.id.btn_back)?.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // Logout: clear login flag and return to LoginActivity
        findViewById<android.view.View?>(R.id.btn_logout)?.setOnClickListener {
            getSharedPreferences("bestmeal_prefs", MODE_PRIVATE)
                .edit().clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }

        // Bottom navigation routing
        findViewById<BottomNavigationView?>(R.id.bottom_nav)?.apply {
            selectedItemId = R.id.nav_settings
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> { startActivity(Intent(this@SettingsActivity, MainActivity::class.java)); true }
                    R.id.nav_meals -> { startActivity(Intent(this@SettingsActivity, LogMealActivity::class.java)); true }
                    R.id.nav_water -> { startActivity(Intent(this@SettingsActivity, WaterTrackerActivity::class.java)); true }
                    R.id.nav_settings -> true // already here
                    else -> false
                }
            }
        }
    }
}
