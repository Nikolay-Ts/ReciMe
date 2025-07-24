package com.sonnenstahl.recime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import coil3.Bitmap
import com.sonnenstahl.recime.utils.AppRoutes
import com.sonnenstahl.recime.utils.Client
import com.sonnenstahl.recime.utils.ImageSize
import com.sonnenstahl.recime.utils.TempStorage
import com.sonnenstahl.recime.utils.data.Meal
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * displays the fetched Meals
 *
 * @param navController navigate between screens
 * @param isRecipeFinder true if there is a filter on
 * @param mealName only used if isRecipeFinder is true
 */
@Composable
fun Recipes(
    navController: NavController,
    isRecipeFinder: Boolean,
    mealName: String?,
) {
    val coroutine = rememberCoroutineScope()
    val meals = remember { mutableStateListOf<Meal?>() }
    val images = remember { mutableStateListOf<Bitmap?>() }
    var refreshing by remember { mutableStateOf(false) }
    val isRecipeFinder by remember { mutableStateOf(isRecipeFinder) }

    LaunchedEffect(Unit) {
        coroutine.launch {
            delay(10000L)
            if (meals.isEmpty()) {
                navController.navigate(AppRoutes.CouldNotLoad.route) {
                    popUpTo(AppRoutes.Home.route) {
                        inclusive = true
                    }
                }
            }
        }

        val cachedSuggestedMeals = TempStorage.suggestedMeals.value
        val cachedSuggestedMealImages = TempStorage.suggestedMealImages.value
        if (cachedSuggestedMeals.isNotEmpty() && cachedSuggestedMealImages.size == cachedSuggestedMeals.size) {
            meals.addAll(cachedSuggestedMeals)
            images.addAll(cachedSuggestedMealImages)
            return@LaunchedEffect
        }

        if (isRecipeFinder == true) {
            var tempMeal: Meal? = null
            if (mealName != null) {
                tempMeal = Client.getMealByName(mealName)
                val meatOptions = TempStorage.meatOptions.value
                val mealOptions = TempStorage.mealOptions.value

                for (meatOption in meatOptions) {
                    if (!meatOption.isChosen.value) {
                        continue
                    }
                    Client.getMealByCategory(meals, meatOption.name)
                }

                for (mealOption in mealOptions) {
                    if (!mealOption.isChosen.value) {
                        continue
                    }
                    Client.getMealByCategory(meals, mealOption.name)
                }

                if (tempMeal?.strMeal != null) { // could not find anything that matches the description
                    meals.filter { it?.strMeal?.contains(tempMeal.strMeal) == true }
                }
                val fetchedImages =
                    meals.map { meal ->
                        Client.getImage(
                            mealName = meal?.strMealThumb ?: "",
                            imageSize = ImageSize.SMALL,
                        )
                    }
                images.addAll(fetchedImages)
            }
            return@LaunchedEffect
        }

        Client.getRandomMeals(meals)
        val fetchedImages =
            meals.map { meal ->
                Client.getImage(
                    mealName = meal?.strMealThumb ?: "",
                    imageSize = ImageSize.SMALL,
                )
            }
        images.addAll(fetchedImages)
    }

    LaunchedEffect(refreshing) {
        if (refreshing) {
            meals.clear()
            images.clear()
            Client.getRandomMeals(meals)

            for (meal in meals) {
                images.add(
                    Client.getImage(
                        mealName = meal?.strMealThumb ?: "",
                        imageSize = ImageSize.SMALL,
                    ),
                )
            }
            refreshing = false
        }
    }

    if (meals.size != images.size || meals.isEmpty()) {
        Loading()
        return
    }

    RecipesList(
        meals = meals,
        images = images,
        refreshing = refreshing,
        onRefresh = { refreshing = true },
    ) { meal ->
        TempStorage.updateChosenMeal(meal)
        TempStorage.updateSuggestedMeals(meals)
        TempStorage.updateSuggestedMealImages(images)
        navController.navigate("${AppRoutes.Recipe.route}/${meal.strMeal}")
    }
}
