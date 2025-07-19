package com.sonnenstahl.recime.utils.data

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

data class MeatOption (
    val name: String,
    var isChosen: Boolean
)

fun initMeatOptions(): List<MeatOption> = MEAT_OPTION_NAMES.map { MeatOption(it, false) }
