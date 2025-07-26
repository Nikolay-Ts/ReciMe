package com.sonnenstahl.recime.utils

import coil3.Bitmap
import com.sonnenstahl.recime.utils.data.Ingredient
import com.sonnenstahl.recime.utils.data.Meal
import com.sonnenstahl.recime.utils.data.MealOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * class used to minimise GET requests to the API
 * and efficiently pass variables between views
 *
 * @property chosenMeal the current meal that has been stored to be displayed
 * @property chosenMealImg the image to correspond
 * @property suggestedMeals the array coming in from the search recipe
 * @property suggestedMealImages the corresponding array of images
 */
object TempStorage {
    private val _chosenMeal = MutableStateFlow<Meal?>(null)
    private val _chosenMealImg = MutableStateFlow<Bitmap?>(null)

    private val _suggestedMeals = MutableStateFlow<List<Meal?>>(emptyList())
    private val _suggestedMealImages = MutableStateFlow<List<Bitmap?>>(emptyList())

    private val _meatOptions = MutableStateFlow<List<MealOption>>(emptyList())
    private val _mealOptions = MutableStateFlow<List<MealOption>>(emptyList())

    private val _ingredients = MutableStateFlow<List<String>>(emptyList())

    val chosenMeal
        get() = _chosenMeal.asStateFlow()

    val chosenMealImg
        get() = _chosenMealImg.asStateFlow()

    val suggestedMeals
        get() = _suggestedMeals.asStateFlow()

    val suggestedMealImages
        get() = _suggestedMealImages.asStateFlow()

    val meatOptions
        get() = _meatOptions.asStateFlow()

    val mealOptions
        get() = _mealOptions.asStateFlow()

    val ingredients
        get() = _ingredients.asStateFlow()

    fun updateChosenMeal(meal: Meal?) {
        _chosenMeal.value = meal
    }

    fun updateSuggestedMeals(meals: List<Meal?>) {
        _suggestedMeals.value = meals
    }

    fun updateSuggestedMealImages(images: List<Bitmap?>) {
        _suggestedMealImages.value = images
    }

    fun clearSuggestedMeals() {
        _suggestedMeals.value = emptyList()
        _suggestedMealImages.value = emptyList()
    }

    fun updateMeatOptions(meatOption: List<MealOption>) {
        _meatOptions.value = meatOption
    }

    fun updateMealOptions(mealOption: List<MealOption>) {
        _mealOptions.value = mealOption
    }

    fun updateIngredients(ingredients: List<String>) {
        _ingredients.value = ingredients
    }

}
