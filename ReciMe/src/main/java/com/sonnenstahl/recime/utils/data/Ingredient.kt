package com.sonnenstahl.recime.utils.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * data class to represent the ingredients in the fridge
 *
 * @param id UUID to parse it to the LazyVerticalGrid
 * @param name given by the user or added from the recipes, can have typos or be in plural
 * @param isSelected which ingredients to use for the query, cannot be more than 10
 * @param filePath of the image. There will be an algorithm to map the word to the closest image
 */
@Serializable
data class Ingredient(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    var isSelected: MutableState<Boolean> = mutableStateOf(false),
    val filePath: String = "file:///android_asset/default-ingredient.png",
)

val TEST_VEGGIES_LIST: List<Ingredient> =
    listOf(
        Ingredient(name = "Carrot"),
        Ingredient(name = "Broccoli"),
        Ingredient(name = "Spinach"),
        Ingredient(name = "Tomato"),
        Ingredient(name = "Onion"),
        Ingredient(name = "Bell Pepper"),
        Ingredient(name = "Potato"),
        Ingredient(name = "Cucumber"),
        Ingredient(name = "Zucchini"),
        Ingredient(name = "Lettuce"),
        Ingredient(name = "Mushroom"),
        Ingredient(name = "Garlic"),
        Ingredient(name = "Corn"),
        Ingredient(name = "Green Bean"),
        Ingredient(name = "Cabbage"),
        Ingredient(name = "Broccoli"),
        Ingredient(name = "Spinach"),
        Ingredient(name = "Tomato"),
        Ingredient(name = "Onion"),
        Ingredient(name = "Bell Pepper"),
        Ingredient(name = "Potato"),
        Ingredient(name = "Cucumber"),
        Ingredient(name = "Zucchini"),
        Ingredient(name = "Lettuce"),
        Ingredient(name = "Mushroom"),
        Ingredient(name = "Garlic"),
        Ingredient(name = "Corn"),
        Ingredient(name = "Green Bean"),
        Ingredient(name = "Cabbage"),
    )
