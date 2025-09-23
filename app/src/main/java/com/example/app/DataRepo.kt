package com.example.app

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

object DataRepo {
    private const val PREF = "bestmeal_prefs"
    private const val KEY_MEALS = "meals_json"
    private const val KEY_WATER_ML = "water_ml"

    data class Meal(
        val id: String = UUID.randomUUID().toString(),
        val name: String,
        val quantity: String,
        val timestamp: Long = System.currentTimeMillis()
    )

    fun addMeal(ctx: Context, meal: Meal) {
        val meals = getMeals(ctx).toMutableList()
        meals.add(0, meal) // add newest first
        saveMeals(ctx, meals)
    }

    fun deleteMeal(ctx: Context, id: String) {
        val meals = getMeals(ctx).filterNot { it.id == id }
        saveMeals(ctx, meals)
    }

    fun getMeals(ctx: Context): List<Meal> {
        val sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val json = sp.getString(KEY_MEALS, "") ?: ""
        if (json.isEmpty()) return emptyList()
        return try {
            val arr = JSONArray(json)
            (0 until arr.length()).map { i ->
                val o = arr.getJSONObject(i)
                Meal(
                    id = o.optString("id"),
                    name = o.optString("name"),
                    quantity = o.optString("quantity"),
                    timestamp = o.optLong("timestamp")
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveMeals(ctx: Context, meals: List<Meal>) {
        val arr = JSONArray()
        meals.forEach { m ->
            val o = JSONObject()
            o.put("id", m.id)
            o.put("name", m.name)
            o.put("quantity", m.quantity)
            o.put("timestamp", m.timestamp)
            arr.put(o)
        }
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit().putString(KEY_MEALS, arr.toString()).apply()
    }

    fun addWater(ctx: Context, ml: Int) {
        val sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val current = sp.getInt(KEY_WATER_ML, 0)
        sp.edit().putInt(KEY_WATER_ML, (current + ml).coerceAtLeast(0)).apply()
    }

    fun getWaterMl(ctx: Context): Int {
        return ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .getInt(KEY_WATER_ML, 0)
    }

    fun setWaterMl(ctx: Context, ml: Int) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit().putInt(KEY_WATER_ML, ml.coerceAtLeast(0)).apply()
    }
}
