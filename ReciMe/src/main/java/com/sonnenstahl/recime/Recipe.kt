package com.sonnenstahl.recime

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sonnenstahl.recime.utils.AppRoutes
import com.sonnenstahl.recime.utils.Client
import com.sonnenstahl.recime.utils.ImageSize
import com.sonnenstahl.recime.utils.IngredientFileManager
import com.sonnenstahl.recime.utils.TempStorage
import com.sonnenstahl.recime.utils.data.Ingredient
import com.sonnenstahl.recime.utils.data.Meal
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * This view is to show the [Meal] that is chosen by the user. The view fetches the [Meal] from
 * the backend but if it was seen again without going home it will load it from the [TempStorage]
 * to prevent unnecessary fetch requests
 *
 * @param navController to navigate between views
 * @param mealName name of the chosen [Meal]
 */
@Composable
fun Recipe(
    navController: NavController,
    mealName: String?,
) {
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    val meal = remember { mutableStateOf<Meal?>(TempStorage.chosenMeal.value) }
    val imageBitmap = remember { mutableStateOf<Bitmap?>(TempStorage.chosenMealImg.value) }
    val ingredients = remember { mutableStateListOf<String>() }
    var isRecipeDisplayed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        coroutine.launch {
            delay(5000L)
            if (meal.value == null) {
                navController.navigate(AppRoutes.CouldNotLoad.route) {
                    popUpTo(AppRoutes.Home.route) {
                        inclusive = true
                    }
                }
            }
        }

        if (meal.value != null) {
            val tempMealResult = coroutine.async { Client.getMealByName(meal.value!!.strMeal) }
            val tempMeal = tempMealResult.await()

            if (tempMeal != null) {
                meal.value = tempMeal
                ingredients.clear()
                ingredients.addAll(tempMeal.ingredients ?: emptyList())
                Log.d("INGREDIENTS", "Fetched: ${tempMeal.ingredients}")
            }
        } else if (mealName == null) {
            meal.value = Client.getRandomMeal()
        } else {
            meal.value = Client.getMealByName(mealName)
        }

        ingredients.addAll(meal.value?.ingredients ?: emptyList())
    }

    LaunchedEffect(meal.value) {
        imageBitmap.value =
            Client.getImage(
                mealName = "${meal.value?.strMealThumb}",
                imageSize = ImageSize.LARGE,
            )
    }

    if (meal.value == null || imageBitmap.value == null) {
        Loading()
        return
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "${meal.value?.strMeal}",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (imageBitmap.value != null) {
                Image(
                    bitmap = imageBitmap.value!!.asImageBitmap(),
                    contentDescription = "Recipe Image",
                    modifier =
                        Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(25.dp)),
                )
            }
        }

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Button(
                onClick = { isRecipeDisplayed = !isRecipeDisplayed },
                modifier = Modifier.padding(top = 16.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Ingredients")
                    Spacer(modifier = Modifier.size(8.dp))
                    Icon(
                        imageVector = if (isRecipeDisplayed) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowRight,
                        contentDescription = "Toggle ingredients",
                    )
                }
            }

            if (isRecipeDisplayed) {
                Card(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        ),
                ) {
                    LazyColumn(
                        modifier =
                            Modifier
                                .padding(16.dp)
                                .height(250.dp),
                    ) {
                        itemsIndexed(meal.value?.ingredients.orEmpty()) { index, ingredient ->
                            val amount = meal.value?.measures?.getOrNull(index)
                            Row(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    text = ingredient,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Text(
                                    text = amount.orEmpty(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 50.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            val buttonModifier = Modifier.padding(horizontal = 10.dp)

            OutlinedButton(
                modifier = buttonModifier,
                onClick = {
                    TempStorage.updateIngredients(emptyList())
                    navController.navigate(AppRoutes.Home.route)
                },
            ) { Text("Back Home") }

            Button(
                modifier = buttonModifier,
                onClick = {
                    coroutine.launch {
                        val existingFridgeIngredients = IngredientFileManager.loadData(context).toMutableList()
                        val currentMealIngredientNames = meal.value?.ingredients.orEmpty()
                        val newIngredientsToAdd = mutableListOf<Ingredient>()
                        currentMealIngredientNames.forEach { newIngredientName ->
                            if (existingFridgeIngredients.none { it.name.equals(newIngredientName, ignoreCase = true) }) {
                                newIngredientsToAdd.add(
                                    Ingredient(name = newIngredientName, isSelected = mutableStateOf(false)),
                                )
                            } else {
                                Log.d("Recipe", "Skipping '$newIngredientName' as it's already in fridge.")
                            }
                        }

                        existingFridgeIngredients.addAll(newIngredientsToAdd)
                        IngredientFileManager.saveData(context, existingFridgeIngredients)
                        TempStorage.updateChosenMeal(null)
                        TempStorage.updateIngredients(emptyList())

                        navController.navigate(AppRoutes.Fridge.route)
                    }
                },
            ) { Text("Add to Fridge") }
        }
    }
}
