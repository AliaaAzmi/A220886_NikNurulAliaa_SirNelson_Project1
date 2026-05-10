package com.example.a220886_niknurulaliaa_sirnelson_project1

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt

data class FoodItem(
    val name: String,
    val calories: Int,
    val mealType: String // Breakfast, Lunch, Dinner
)

class SwiftBiteViewModel : ViewModel() {

    var foodLog = mutableStateListOf<FoodItem>()
        private set

    // ── Setup ────────────────────────────────────────────────────────────
    var isProfileSetup by mutableStateOf(false)
    var userName by mutableStateOf("")
    var gender by mutableStateOf("")
    var age by mutableStateOf(0)
    var weight by mutableStateOf(0f)
    var height by mutableStateOf(0f)
    var activityLevel by mutableStateOf("")
    var weightGoal by mutableStateOf("")
    var calorieGoal by mutableStateOf(2000)
    val stepGoal = "10,000 steps"
    val waterGoal = "8 glasses"

    // ── 1. Motivational Quotes ───────────────────────────────────────────
    private val quoteList = listOf(
        "\"Your health is an investment, not an expense.\"",
        "\"Take care of your body. It's the only place you have to live.\"",
        "\"Small steps every day lead to big results.\"",
        "\"Eat well, live well, be well.\"",
        "\"A healthy outside starts from the inside.\"",
        "\"You don't have to be great to start, but you have to start to be great.\"",
        "\"Every bite counts. Make it worth it.\"",
        "\"Discipline is choosing between what you want now and what you want most.\""
    )

    // Quote bertukar setiap kali ViewModel dicipta (setiap kali app dibuka)
    val dailyQuote: String = quoteList.random()

    // ── 2. Dark Mode Toggle ──────────────────────────────────────────────
    var isDarkMode by mutableStateOf(false)

    // ── 5. Water Intake Tracker ──────────────────────────────────────────
    var waterIntake by mutableStateOf(0)        // bilangan gelas
    val waterGoalCount = 8                       // target 8 gelas

    fun addWater() {
        if (waterIntake < waterGoalCount) waterIntake++
    }

    fun removeWater() {
        if (waterIntake > 0) waterIntake--
    }

    // ── Calorie Calculation (Mifflin-St Jeor) ───────────────────────────
    fun calculateCalorieGoal() {
        val bmr = if (gender == "Female") {
            (10 * weight) + (6.25f * height) - (5 * age) - 161
        } else {
            (10 * weight) + (6.25f * height) - (5 * age) + 5
        }
        val activityMultiplier = when (activityLevel) {
            "Sedentary"         -> 1.2f
            "Lightly Active"    -> 1.375f
            "Moderately Active" -> 1.55f
            "Very Active"       -> 1.725f
            else                -> 1.2f
        }
        val maintenance = bmr * activityMultiplier
        val finalCalories = when (weightGoal) {
            "Lose Weight"     -> maintenance - 500
            "Maintain Weight" -> maintenance
            "Gain Weight"     -> maintenance + 500
            else              -> maintenance
        }
        calorieGoal = finalCalories.roundToInt().coerceAtLeast(1200)
    }

    // ── Macro Calculation ────────────────────────────────────────────────
    fun getProteinGrams(): Int {
        val pct = when (weightGoal) {
            "Lose Weight"     -> 0.35f
            "Maintain Weight" -> 0.25f
            "Gain Weight"     -> 0.30f
            else              -> 0.25f
        }
        return ((calorieGoal * pct) / 4).roundToInt()
    }

    fun getCarbsGrams(): Int {
        val pct = when (weightGoal) {
            "Lose Weight"     -> 0.35f
            "Maintain Weight" -> 0.50f
            "Gain Weight"     -> 0.50f
            else              -> 0.50f
        }
        return ((calorieGoal * pct) / 4).roundToInt()
    }

    fun getFatGrams(): Int {
        val pct = when (weightGoal) {
            "Lose Weight"     -> 0.30f
            "Maintain Weight" -> 0.25f
            "Gain Weight"     -> 0.20f
            else              -> 0.25f
        }
        return ((calorieGoal * pct) / 9).roundToInt()
    }

    // ── BMI ──────────────────────────────────────────────────────────────
    fun getBMI(): Float {
        if (height == 0f) return 0f
        val h = height / 100f
        return weight / (h * h)
    }

    fun getBMIStatus(): String = when {
        getBMI() < 18.5f -> "Underweight"
        getBMI() < 25.0f -> "Normal ✅"
        getBMI() < 30.0f -> "Overweight"
        else             -> "Obese"
    }

    // ── Food Log ─────────────────────────────────────────────────────────
    fun addFood(name: String, calories: Int, mealType: String) {
        foodLog.add(FoodItem(name = name, calories = calories, mealType = mealType))
    }

    fun getTotalCalories(): Int = foodLog.sumOf { it.calories }

    fun getTotalFoodCount(): Int = foodLog.size

    fun getFoodCountByMeal(mealType: String): Int =
        foodLog.count { it.mealType == mealType }
}
