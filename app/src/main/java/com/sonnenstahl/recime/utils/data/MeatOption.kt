package com.sonnenstahl.recime.utils.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

val MEAT_OPTION_NAMES: Array<String> = arrayOf(
    "Beef",
    "Chicken",
    "Lamb",
    "Miscellaneous",
    "Pasta",
    "Pork",
    "Seafood",
    "Vegan",
    "Vegetarian"
)

val excludedNames = setOf("Vegan", "Vegetarian", "Pasta")

data class MeatOption (
    val name: String,
    var isChosen: MutableState<Boolean> = mutableStateOf(false)
)

fun initMeatOptions(): List<MeatOption> = MEAT_OPTION_NAMES.map { MeatOption(it) }

fun disableMeat(meatOptions: List<MeatOption>) =
    meatOptions
        .filter { it.name !in excludedNames}
        .forEach { it.isChosen.value = false }

fun disableVegan(meatOptions: List<MeatOption>) =
    meatOptions
        .filter { it.name in excludedNames.take(2) }
        .forEach { it.isChosen.value = false }

