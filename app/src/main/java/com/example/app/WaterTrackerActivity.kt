package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class WaterTrackerActivity : AppCompatActivity() {

    private lateinit var progressText: TextView
    private val drops = mutableListOf<ImageView>()
    private var filled = 0
    private val total = 12 // 12 droplets (250ml each) => 3L goal (250ml per drop)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_water_tracker)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        progressText = findViewById(R.id.tv_progress)
        initDrops()
        // Initialize from saved ml
        val savedMl = DataRepo.getWaterMl(this)
        filled = (savedMl / 250).coerceIn(0, total)
        updateUI()

        findViewById<android.view.View>(R.id.btn_add_250)?.setOnClickListener {
            addDrops(1)
        }
        findViewById<android.view.View>(R.id.btn_add_500)?.setOnClickListener {
            addDrops(2)
        }

        // Bottom navigation routing
        findViewById<BottomNavigationView?>(R.id.bottom_nav)?.apply {
            selectedItemId = R.id.nav_water
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> { startActivity(Intent(this@WaterTrackerActivity, MainActivity::class.java)); true }
                    R.id.nav_meals -> { startActivity(Intent(this@WaterTrackerActivity, LogMealActivity::class.java)); true }
                    R.id.nav_water -> true // already here
                    R.id.nav_settings -> true
                    else -> false
                }
            }
        }
    }

    private fun initDrops() {
        val ids = listOf(
            R.id.drop0, R.id.drop1, R.id.drop2, R.id.drop3,
            R.id.drop4, R.id.drop5, R.id.drop6, R.id.drop7,
            R.id.drop8, R.id.drop9, R.id.drop10, R.id.drop11
        )
        ids.forEachIndexed { index, id ->
            val iv = findViewById<ImageView>(id)
            iv.setOnClickListener { toggleTo(index) }
            drops += iv
        }
    }

    private fun toggleTo(index: Int) {
        // Fill up to index inclusively if currently below, else unfill down to index-1
        val targetCount = if (index + 1 > filled) index + 1 else index
        filled = targetCount.coerceIn(0, total)
        DataRepo.setWaterMl(this, filled * 250)
        updateUI()
    }

    private fun addDrops(count: Int) {
        filled = (filled + count).coerceAtMost(total)
        DataRepo.setWaterMl(this, filled * 250)
        updateUI()
    }

    private fun updateUI() {
        progressText.text = "$filled/$total"
        drops.forEachIndexed { i, iv ->
            iv.setImageResource(if (i < filled) R.drawable.ic_drop_filled else R.drawable.ic_drop_outline)
        }
    }
}
