package com.example.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class DailySummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_daily_summary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // CTA to log meal
        findViewById<android.view.View?>(R.id.btn_log_your_meal)?.setOnClickListener {
            startActivity(Intent(this, LogMealActivity::class.java))
        }

        // Bottom navigation
        findViewById<BottomNavigationView?>(R.id.bottom_nav)?.apply {
            selectedItemId = R.id.nav_meals
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> { startActivity(Intent(this@DailySummaryActivity, MainActivity::class.java)); true }
                    R.id.nav_meals -> { startActivity(Intent(this@DailySummaryActivity, LogMealActivity::class.java)); true }
                    R.id.nav_water -> { true }
                    R.id.nav_settings -> { true }
                    else -> false
                }
            }
        }
    }
}
