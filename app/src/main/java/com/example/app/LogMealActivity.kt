package com.example.app

import android.os.Bundle
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText

class LogMealActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_log_meal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Back button
        findViewById<android.view.View?>(R.id.btn_back)?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Add Meal
        findViewById<android.view.View?>(R.id.btn_submit_meal)?.setOnClickListener {
            val name = findViewById<TextInputEditText?>(R.id.et_meal_name)?.text?.toString()?.trim().orEmpty()
            val qty = findViewById<TextInputEditText?>(R.id.et_quantity)?.text?.toString()?.trim().orEmpty()
            if (name.isNotEmpty() && qty.isNotEmpty()) {
                DataRepo.addMeal(this, DataRepo.Meal(name = name, quantity = qty))
                startActivity(Intent(this, DailySummaryActivity::class.java))
                finish()
            } else {
                // no-op minimal: could show a Toast, keeping UI minimal per brief
            }
        }

        // Bottom navigation
        findViewById<BottomNavigationView?>(R.id.bottom_nav)?.apply {
            selectedItemId = R.id.nav_meals
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> { startActivity(Intent(this@LogMealActivity, MainActivity::class.java)); finish(); true }
                    R.id.nav_meals -> true // already here
                    R.id.nav_water -> { startActivity(Intent(this@LogMealActivity, WaterTrackerActivity::class.java)); true }
                    R.id.nav_settings -> { startActivity(Intent(this@LogMealActivity, SettingsActivity::class.java)); true }
                    else -> false
                }
            }
        }
    }
}
