package com.example.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText

class CalorieReferenceActivity : AppCompatActivity() {
    private data class Food(val name: String, val calories: Int)
    private val foods = listOf(
        Food("Boiled Egg 100g", 155),
        Food("Apple (medium)", 95),
        Food("Chicken Breast 100g", 165),
        Food("Whole Wheat Bread (1 slice)", 82),
        Food("Spinach 100g", 23),
        Food("Banana (medium)", 105),
        Food("Greek Yogurt (plain, 100g)", 59),
        Food("Rice (cooked, 100g)", 130),
        Food("Oats (dry, 40g)", 150),
        Food("Almonds (28g)", 164)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calorie_reference)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Render initial list
        renderList(foods)

        // Search
        findViewById<TextInputEditText?>(R.id.et_search_food)?.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val q = s?.toString()?.trim()?.lowercase().orEmpty()
                val filtered = if (q.isEmpty()) foods else foods.filter { it.name.lowercase().contains(q) }
                renderList(filtered)
            }
        })

        // Bottom navigation
        findViewById<BottomNavigationView?>(R.id.bottom_nav)?.apply {
            selectedItemId = R.id.nav_meals
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> { startActivity(Intent(this@CalorieReferenceActivity, MainActivity::class.java)); true }
                    R.id.nav_meals -> { startActivity(Intent(this@CalorieReferenceActivity, LogMealActivity::class.java)); true }
                    R.id.nav_water -> { startActivity(Intent(this@CalorieReferenceActivity, WaterTrackerActivity::class.java)); true }
                    R.id.nav_settings -> { true }
                    else -> false
                }
            }
        }
    }

    private fun renderList(list: List<Food>) {
        val container = findViewById<LinearLayout?>(R.id.container_foods) ?: return
        container.removeAllViews()
        val inflater = LayoutInflater.from(this)
        list.forEach { f ->
            val row = inflater.inflate(R.layout.item_food_calorie, container, false)
            row.findViewById<TextView>(R.id.tv_food_title).text = f.name
            row.findViewById<TextView>(R.id.tv_food_cal).text = "${f.calories} Calories"
            container.addView(row)
        }
    }
}
