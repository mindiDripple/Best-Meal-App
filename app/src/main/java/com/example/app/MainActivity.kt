package com.example.app

import android.os.Bundle
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Buttons
        findViewById<android.view.View?>(R.id.btn_add_meal)?.setOnClickListener {
            startActivity(Intent(this, LogMealActivity::class.java))
        }
        findViewById<android.view.View?>(R.id.btn_daily_summary)?.setOnClickListener {
            startActivity(Intent(this, DailySummaryActivity::class.java))
        }
        findViewById<android.view.View?>(R.id.btn_calorie_ref)?.setOnClickListener {
            startActivity(Intent(this, CalorieReferenceActivity::class.java))
        }

        // Log Water button -> WaterTracker
        findViewById<android.view.View?>(R.id.btn_log_water)?.setOnClickListener {
            startActivity(Intent(this, WaterTrackerActivity::class.java))
        }

        // Stat cards
        findViewById<android.view.View?>(R.id.card_calories)?.setOnClickListener {
            startActivity(Intent(this, CalorieReferenceActivity::class.java))
        }
        findViewById<android.view.View?>(R.id.card_water)?.setOnClickListener {
            startActivity(Intent(this, WaterTrackerActivity::class.java))
        }

        // Bottom navigation
        findViewById<BottomNavigationView?>(R.id.bottom_nav)?.apply {
            selectedItemId = R.id.nav_home
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> true // already here
                    R.id.nav_meals -> {
                        startActivity(Intent(this@MainActivity, LogMealActivity::class.java))
                        true
                    }
                    R.id.nav_water -> { startActivity(Intent(this@MainActivity, WaterTrackerActivity::class.java)); true }
                    R.id.nav_settings -> { startActivity(Intent(this@MainActivity, SettingsActivity::class.java)); true }
                    else -> false
                }
            }
        }
    }
}