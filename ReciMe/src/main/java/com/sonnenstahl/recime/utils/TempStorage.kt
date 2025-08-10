package com.sonnenstahl.recime.utils

import coil3.Bitmap
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

    /**
     * @property chosenMeal the currently chosen meal
     */
    val chosenMeal
        get() = _chosenMeal.asStateFlow()

    /**
     * @property chosenMealImg that corresponds to [chosenMeal]
     */
    val chosenMealImg
        get() = _chosenMealImg.asStateFlow()

    /**
     * @property suggestedMeals to show when the user goes back from [com.sonnenstahl.recime.Recipe] to
     * [AppRoutes.Recipes] to avoid a fetch request
     */
    val suggestedMeals
        get() = _suggestedMeals.asStateFlow()

    /**
     * @property suggestedMealImages that correspond to [suggestedMeals]
     */
    val suggestedMealImages
        get() = _suggestedMealImages.asStateFlow()

    /**
     * @property meatOptions the chosen meat options from [AppRoutes.RecipeFinder] going to
     * [com.sonnenstahl.recime.Recipes]
     */
    val meatOptions
        get() = _meatOptions.asStateFlow()

    /**
     * @property mealOptions the chosen meal options from [AppRoutes.RecipeFinder] going to
     * [com.sonnenstahl.recime.Recipes]
     */
    val mealOptions
        get() = _mealOptions.asStateFlow()

    /**
     * @property mealOptions the chosen ingredients from [com.sonnenstahl.recime.Fridge] going to
     * [com.sonnenstahl.recime.Recipes]
     */
    val ingredients
        get() = _ingredients.asStateFlow()

    /**
     * set it in the cache to not have to load it again going from [com.sonnenstahl.recime.Recipes]
     * to [com.sonnenstahl.recime.Recipe]
     *
     * @param meal that is chosen from the user
     */
    fun updateChosenMeal(meal: Meal?) {
        _chosenMeal.value = meal
    }

    /**
     * set it in the cache to not have to load it again going from [com.sonnenstahl.recime.Recipes]
     * to [com.sonnenstahl.recime.Recipe]
     *
     * @param image that corresponds to the chosen meal
     */
    fun updateChosenMealImage(image: Bitmap?) {
        _chosenMealImg.value = image
    }

    /**
     * set it in the cache to not have to load it again going from [com.sonnenstahl.recime.Recipes]
     * to [com.sonnenstahl.recime.Recipe]
     *
     * @param meals that are chosen from the user
     */
    fun updateSuggestedMeals(meals: List<Meal?>) {
        _suggestedMeals.value = meals
    }

    /**
     * set it in the cache to not have to load it again going from [com.sonnenstahl.recime.Recipes]
     * to [com.sonnenstahl.recime.Recipe]
     *
     * @param images that corresponds to the chosen meals
     */
    fun updateSuggestedMealImages(images: List<Bitmap?>) {
        _suggestedMealImages.value = images
    }

    /**
     * remove the suggested meals and their images
     */
    fun clearSuggestedMeals() {
        _suggestedMeals.value = emptyList()
        _suggestedMealImages.value = emptyList()
    }

    /**
     * update the meat options going
     *
     * @param meatOption chosen by the user
     */
    fun updateMeatOptions(meatOption: List<MealOption>) {
        _meatOptions.value = meatOption
    }

    /**
     * update the meal options going
     *
     * @param mealOption chosen by the user
     */
    fun updateMealOptions(mealOption: List<MealOption>) {
        _mealOptions.value = mealOption
    }

    /**
     * update the ingredients to be searched for
     *
     * @param ingredients chosen by the user
     */
    fun updateIngredients(ingredients: List<String>) {
        _ingredients.value = ingredients
    }
}
