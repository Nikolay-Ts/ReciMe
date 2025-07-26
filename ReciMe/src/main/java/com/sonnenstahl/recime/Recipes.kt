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
import com.sonnenstahl.recime.utils.data.SearchType
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
    searchType: SearchType,
    mealName: String?,
) {
    val coroutine = rememberCoroutineScope()
    val meals = remember { mutableStateListOf<Meal?>() }
    val images = remember { mutableStateListOf<Bitmap?>() }
    var refreshing by remember { mutableStateOf(false) }
    val searchType by remember { mutableStateOf(searchType) }

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

        when (searchType) {
            SearchType.CATEGORY -> {
                var tempMeal: Meal? = null
                if (mealName != null) {
                    tempMeal = Client.getMealByName(mealName)
                    val meatOptions = TempStorage.meatOptions.value
                    val mealOptions = TempStorage.mealOptions.value

                    val deferredMeatCategoryFetches = meatOptions
                        .filter { it.isChosen.value }
                        .map { meatOption ->
                            coroutine.async {
                                Client.getMealByCategory(meals, meatOption.name)
                            }
                        }
                    deferredMeatCategoryFetches.awaitAll()

                    for (mealOption in mealOptions) {
                        if (!mealOption.isChosen.value) {
                            continue
                        }
                        Client.getMealByCategory(meals, mealOption.name)
                    }

                    if (tempMeal?.strMeal != null) { // could not find anything that matches the description
                        meals.add(tempMeal)
                        meals.filter { it?.strMeal?.contains(tempMeal.strMeal) == true }
                    }


                }
            }

            SearchType.INGREDIENTS -> {
                val ingredients = TempStorage.ingredients.value

                val deferredMealFetches = ingredients.map { ingredient ->
                    coroutine.async {
                        Client.getMealByIngredient(meals, ingredient)
                    }
                }
                deferredMealFetches.awaitAll()
            }

            SearchType.NONE -> {
                Client.getRandomMeals(meals)
            }

        }

        meals.shuffle()

        val fetchedImages =
            meals.map { meal ->
                coroutine.async {
                    Client.getImage(
                        mealName = meal?.strMealThumb ?: "",
                        imageSize = ImageSize.SMALL,
                    )
                }
            }
        images.addAll(fetchedImages.awaitAll())
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
