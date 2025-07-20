package com.sonnenstahl.recime

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
import com.sonnenstahl.recime.utils.data.RandomMeal

/**
 * displays the fetched Meals
 *
 * @param navController navigate between screens
 */
@Composable
fun Recipes(navController: NavController) {
    val randomMeals = remember { mutableStateListOf<RandomMeal?>() }
    val images = remember { mutableStateListOf<Bitmap?>() }
    var refreshing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        Client.getRandomMeals(randomMeals)
        for (meal in randomMeals) {
            images.add(
                Client.getImage(
                    mealName = meal?.strMealThumb ?: "",
                    imageSize = ImageSize.SMALL,
                ),
            )
        }
    }

    LaunchedEffect(refreshing) {
        if (refreshing) {
            randomMeals.clear()
            images.clear()
            Client.getRandomMeals(randomMeals)

            for (meal in randomMeals) {
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

    if (randomMeals.size != images.size || randomMeals.isEmpty()) {
        Loading()
        return
    }

    RecipesList(
        meals = randomMeals,
        images = images,
        refreshing = refreshing,
        onRefresh = { refreshing = true }
    ) { mealName -> navController.navigate("${AppRoutes.Recipe.route}/${mealName}") }
}
