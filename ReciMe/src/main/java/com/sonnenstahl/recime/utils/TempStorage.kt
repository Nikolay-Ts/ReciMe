package com.sonnenstahl.recime.utils

import coil3.Bitmap
import com.sonnenstahl.recime.utils.data.Meal
import com.sonnenstahl.recime.utils.data.MealOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * class used to minimise GET requests to the API
 * and efficiently pass variables between views
 *
 * @property chosenMeal the current meal that has been stored to be displayed
 */
object TempStorage {
    private val _chosenMeal = MutableStateFlow<Meal?>(null)
    private val _chosenMealImg = MutableStateFlow<Bitmap?>(null)
    private val _meatOptions = MutableStateFlow<List<MealOption?>>(emptyList())
    private val _mealOptions = MutableStateFlow<List<MealOption?>>(emptyList())

    val chosenMeal
        get() = _chosenMeal.asStateFlow()

    val chosenMealImg
        get() = _chosenMealImg.asStateFlow()

    val meatOptions
        get() = _meatOptions.asStateFlow()

    val mealOptions
        get() = _mealOptions.asStateFlow()


    fun updateChosenMeal(meal: Meal?) {
        _chosenMeal.value = meal
    }

    fun updateMeatOptions(meatOption: List<MealOption?>) {
        _meatOptions.value = meatOption
    }
    fun updateMealOptions(mealOption: List<MealOption?>) {
        _mealOptions.value = mealOption
    }
}
