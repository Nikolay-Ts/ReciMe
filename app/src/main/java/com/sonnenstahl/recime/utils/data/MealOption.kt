package com.sonnenstahl.recime.utils.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

val MEAT_OPTION_NAMES: Array<String> =
    arrayOf(
        "Beef",
        "Chicken",
        "Lamb",
        "Pasta",
        "Pork",
        "Seafood",
        "Vegetarian",
        "Vegan",
        "Miscellaneous",
    )

val MEAL_TYPE_OPTION: Array<String> = arrayOf("Breakfast", "Main", "Desert")

val EXCLUDED_NAMES = setOf("Vegan", "Vegetarian", "Pasta")
val VEGGIES = setOf("Vegan", "Vegetarian")

data class MealOption(
    val name: String,
    var isChosen: MutableState<Boolean> = mutableStateOf(false),
)

fun initMeatOptions(): List<MealOption> = MEAT_OPTION_NAMES.map { MealOption(it) }

fun initMealTypeOptions(): List<MealOption> = MEAL_TYPE_OPTION.map { MealOption(it) }

fun disableMeat(meatOptions: List<MealOption>) =
    meatOptions
        .filter { it.name !in EXCLUDED_NAMES }
        .forEach { it.isChosen.value = false }

fun disableVegan(meatOptions: List<MealOption>) =
    meatOptions
        .filter { it.name in VEGGIES }
        .forEach { it.isChosen.value = false }

fun disableAllButVegan(meatOptions: List<MealOption>) =
    meatOptions
        .filter { it.name != "Vegan" }
        .forEach { it.isChosen.value = false }
