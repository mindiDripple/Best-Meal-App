package com.example.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class DailySummaryEmptyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_daily_summary_empty)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Button: Log your first meal -> LogMealActivity
        findViewById<android.view.View?>(R.id.btn_log_first_meal)?.setOnClickListener {
            startActivity(Intent(this, LogMealActivity::class.java))
        }

        // Bottom navigation: Home -> MainActivity, Meals -> LogMealActivity
        findViewById<BottomNavigationView?>(R.id.bottom_nav)?.apply {
            selectedItemId = R.id.nav_meals
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> { startActivity(Intent(this@DailySummaryEmptyActivity, MainActivity::class.java)); true }
                    R.id.nav_meals -> { startActivity(Intent(this@DailySummaryEmptyActivity, LogMealActivity::class.java)); true }
                    R.id.nav_water -> { startActivity(Intent(this@DailySummaryEmptyActivity, WaterTrackerActivity::class.java)); true }
                    R.id.nav_settings -> { startActivity(Intent(this@DailySummaryEmptyActivity, SettingsActivity::class.java)); true }
                    else -> false
                }
            }
        }
    }
}
