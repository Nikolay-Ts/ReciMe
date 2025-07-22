package com.sonnenstahl.recime

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import coil3.Bitmap
import com.sonnenstahl.recime.utils.AppRoutes
import com.sonnenstahl.recime.utils.Client
import com.sonnenstahl.recime.utils.ImageSize
import com.sonnenstahl.recime.utils.TempStorage
import com.sonnenstahl.recime.utils.data.Meal

/**
 * displays the fetched Meals
 *
 * @param navController navigate between screens
 */
@Composable
fun Recipes(
    navController: NavController,
    isRecipeFinder: Boolean,
    mealName: String?
) {
    val meals = remember { mutableStateListOf<Meal?>() }
    val images = remember { mutableStateListOf<Bitmap?>() }
    var refreshing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val cachedSuggestedMeals = TempStorage.suggestedMeals.value
        val cachedSuggestedMealImages = TempStorage.suggestedMealImages.value
        if (cachedSuggestedMeals.isNotEmpty() && cachedSuggestedMealImages.size == cachedSuggestedMeals.size) {
            meals.addAll(cachedSuggestedMeals)
            images.addAll(cachedSuggestedMealImages)
            return@LaunchedEffect
        }

        if (isRecipeFinder) {
            var tempMeal: Meal? = null
            if (mealName != null) {
                tempMeal = Client.getMealByName(mealName)
                if (tempMeal == null) { // could not find anything that matches the description
                    //TODO: go to error menu and ask to go back
                    return@LaunchedEffect
                }


                val meatOptions = TempStorage.meatOptions.value
                val mealOptions = TempStorage.mealOptions.value

                for (meatOption in meatOptions) {
                    if (! meatOption.isChosen.value) {
                        continue
                    }
                    Client.getMealByCategory(meals, meatOption.name)
                }

                for (mealOption in mealOptions) {
                    if (! mealOption.isChosen.value) {
                        continue
                    }
                    Client.getMealByCategory(meals, mealOption.name)
                }

                meals.filter { it?.strMeal?.contains(mealName) == true  }

                val fetchedImages = meals.map { meal ->
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
        val fetchedImages = meals.map { meal ->
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
        Log.d("MAPPING", "${TempStorage.suggestedMeals.value}")

        navController.navigate("${AppRoutes.Recipe.route}/${meal.strMeal}")
    }
}
