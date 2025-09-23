package com.example.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView

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

        // Populate meals
        renderMeals()
        // Update water status
        val waterMl = DataRepo.getWaterMl(this)
        findViewById<TextView?>(R.id.tv_water_status)?.text = "Water today: ${waterMl} ml"

        // Bottom navigation
        findViewById<BottomNavigationView?>(R.id.bottom_nav)?.apply {
            selectedItemId = R.id.nav_meals
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> { startActivity(Intent(this@DailySummaryActivity, MainActivity::class.java)); true }
                    R.id.nav_meals -> { startActivity(Intent(this@DailySummaryActivity, LogMealActivity::class.java)); true }
                    R.id.nav_water -> { startActivity(Intent(this@DailySummaryActivity, WaterTrackerActivity::class.java)); true }
                    R.id.nav_settings -> { startActivity(Intent(this@DailySummaryActivity, SettingsActivity::class.java)); true }
                    else -> false
                }
            }
        }
    }

    private fun renderMeals() {
        val meals = DataRepo.getMeals(this)
        if (meals.isEmpty()) {
            startActivity(Intent(this, DailySummaryEmptyActivity::class.java))
            finish()
            return
        }
        val container = findViewById<LinearLayout?>(R.id.container_meals) ?: return
        container.removeAllViews()
        val inflater = LayoutInflater.from(this)
        meals.forEach { meal ->
            val row = inflater.inflate(R.layout.item_logged_meal, container, false)
            row.findViewById<TextView>(R.id.tv_meal_title).text = meal.name
            row.findViewById<TextView>(R.id.tv_meal_sub).text = meal.quantity
            row.findViewById<ImageButton>(R.id.btn_delete).setOnClickListener {
                DataRepo.deleteMeal(this, meal.id)
                renderMeals()
            }
            container.addView(row)
        }
    }
}
