package com.sonnenstahl.recime.utils

import coil3.Bitmap
import com.sonnenstahl.recime.utils.data.Meal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * class used to minimise GET requests to the API
 * and efficiently pass variables between views
 */
object TempStorage {
    private val _chosenMeal = MutableStateFlow<Meal?>(null)
    private val _chosenMealImg = MutableStateFlow<Bitmap?>(null)

    val chosenMeal
        get() = _chosenMeal.asStateFlow()

    val chosenMealImg
        get() = _chosenMealImg.asStateFlow()

    fun updateChosenMeal(meal: Meal?) {
        _chosenMeal.value = meal
    }
}
