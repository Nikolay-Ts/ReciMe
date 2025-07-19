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

val excludedNames = setOf("Vegan", "Vegetarian", "Pasta")
val veggies = setOf("Vegan", "Vegetarian")

data class MeatOption(
    val name: String,
    var isChosen: MutableState<Boolean> = mutableStateOf(false),
)

fun initMeatOptions(): List<MeatOption> = MEAT_OPTION_NAMES.map { MeatOption(it) }

fun disableMeat(meatOptions: List<MeatOption>) =
    meatOptions
        .filter { it.name !in excludedNames }
        .forEach { it.isChosen.value = false }

fun disableVegan(meatOptions: List<MeatOption>) =
    meatOptions
        .filter { it.name in veggies }
        .forEach { it.isChosen.value = false }

fun disableAllButVegan(meatOptions: List<MeatOption>) =
    meatOptions
        .filter { it.name != "Vegan" }
        .forEach { it.isChosen.value = false }
