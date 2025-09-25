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
import android.widget.ImageView
import androidx.core.content.ContextCompat

class CalorieReferenceActivity : AppCompatActivity() {
    private enum class Category { ALL, FRUITS, VEGETABLES, PROTEINS, DESSERTS }
    private data class Food(val name: String, val calories: Int, val category: Category, val imageRes: Int)

    private val foods = listOf(
        // Fruits
        Food("Apple (medium)", 95, Category.FRUITS, R.drawable.fruitlist),
        Food("Banana (medium)", 105, Category.FRUITS, R.drawable.fruitlist),
        Food("Orange (medium)", 62, Category.FRUITS, R.drawable.fruitlist),
        Food("Strawberries 100g", 32, Category.FRUITS, R.drawable.fruitlist),
        Food("Blueberries 100g", 57, Category.FRUITS, R.drawable.fruitlist),
        Food("Grapes 100g", 69, Category.FRUITS, R.drawable.fruitlist),
        // Vegetables
        Food("Spinach 100g", 23, Category.VEGETABLES, R.drawable.img_vegetables),
        Food("Broccoli 100g", 34, Category.VEGETABLES, R.drawable.img_vegetables),
        Food("Carrot 100g", 41, Category.VEGETABLES, R.drawable.img_vegetables),
        Food("Tomato 100g", 18, Category.VEGETABLES, R.drawable.img_vegetables),
        Food("Cucumber 100g", 16, Category.VEGETABLES, R.drawable.img_vegetables),
        Food("Potato (boiled, 100g)", 87, Category.VEGETABLES, R.drawable.img_vegetables),
        // Proteins
        Food("Boiled Egg 100g", 155, Category.PROTEINS, R.drawable.img_proteins),
        Food("Chicken Breast 100g", 165, Category.PROTEINS, R.drawable.img_proteins),
        Food("Greek Yogurt (100g)", 59, Category.PROTEINS, R.drawable.img_proteins),
        Food("Lentils (cooked, 100g)", 116, Category.PROTEINS, R.drawable.img_proteins),
        Food("Tofu (firm, 100g)", 76, Category.PROTEINS, R.drawable.img_proteins),
        Food("Paneer (100g)", 321, Category.PROTEINS, R.drawable.img_proteins),
        // Desserts
        Food("Dark Chocolate (28g)", 155, Category.DESSERTS, R.drawable.img_desserts),
        Food("Ice Cream (1/2 cup)", 137, Category.DESSERTS, R.drawable.img_desserts),
        Food("Cupcake (1 small)", 200, Category.DESSERTS, R.drawable.img_desserts),
        Food("Gulab Jamun (1 pc)", 112, Category.DESSERTS, R.drawable.img_desserts),
        Food("Brownie (1 small)", 129, Category.DESSERTS, R.drawable.img_desserts)
    )

    private var currentCategory: Category = Category.ALL

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
                val filteredByCat = filterByCategory(foods, currentCategory)
                val filtered = if (q.isEmpty()) filteredByCat else filteredByCat.filter { it.name.lowercase().contains(q) }
                renderList(filtered)
            }
        })

        // Chips listeners
        findViewById<TextView?>(R.id.chip_all)?.setOnClickListener { selectCategory(Category.ALL) }
        findViewById<TextView?>(R.id.chip_fruits)?.setOnClickListener { selectCategory(Category.FRUITS) }
        findViewById<TextView?>(R.id.chip_vegetables)?.setOnClickListener { selectCategory(Category.VEGETABLES) }
        findViewById<TextView?>(R.id.chip_proteins)?.setOnClickListener { selectCategory(Category.PROTEINS) }
        findViewById<TextView?>(R.id.chip_desserts)?.setOnClickListener { selectCategory(Category.DESSERTS) }

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
            row.findViewById<ImageView>(R.id.iv_food).setImageDrawable(ContextCompat.getDrawable(this, f.imageRes))
            container.addView(row)
        }
    }

    private fun selectCategory(cat: Category) {
        currentCategory = cat
        // update chip styles
        val chips = listOf(
            R.id.chip_all to Category.ALL,
            R.id.chip_fruits to Category.FRUITS,
            R.id.chip_vegetables to Category.VEGETABLES,
            R.id.chip_proteins to Category.PROTEINS,
            R.id.chip_desserts to Category.DESSERTS,
        )
        chips.forEach { (id, category) ->
            val tv = findViewById<TextView?>(id) ?: return@forEach
            if (category == cat) {
                tv.setBackgroundResource(R.drawable.bg_chip_pill_primary)
                tv.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            } else {
                tv.setBackgroundResource(R.drawable.bg_chip_pill)
                tv.setTextColor(0xFF2E7D32.toInt())
            }
        }
        // filter by category + current search
        val q = findViewById<TextInputEditText?>(R.id.et_search_food)?.text?.toString()?.trim()?.lowercase().orEmpty()
        val filteredByCat = filterByCategory(foods, cat)
        val filtered = if (q.isEmpty()) filteredByCat else filteredByCat.filter { it.name.lowercase().contains(q) }
        renderList(filtered)
    }

    private fun filterByCategory(list: List<Food>, cat: Category): List<Food> =
        when (cat) {
            Category.ALL -> list
            else -> list.filter { it.category == cat }
        }
}
