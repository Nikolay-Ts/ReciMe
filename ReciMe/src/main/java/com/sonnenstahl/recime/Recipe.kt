package com.sonnenstahl.recime

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.Navigator
import coil3.compose.AsyncImage
import com.sonnenstahl.recime.utils.AppRoutes
import com.sonnenstahl.recime.utils.Client
import com.sonnenstahl.recime.utils.ImageSize
import com.sonnenstahl.recime.utils.TempStorage
import com.sonnenstahl.recime.utils.data.Meal

/**
 * Shows the current recipe passed through as a name. There is also a TempStorage
 * which caches the data and if the cache is successful it will just load it in but if it fails
 * it fetches it again by name
 *
 * @param navController used to navigate back to the [Home] view
 * @param name the name of the meal
 */
@Composable
fun Recipe(
    navController: NavController,
    name: String?
) {
    val meal = remember { mutableStateOf<Meal?>(TempStorage.chosenMeal.value) }
    val imageBitmap = remember { mutableStateOf<Bitmap?>(TempStorage.chosenMealImg.value) }
    var isRecipeDisplayed by remember { mutableStateOf(false )}

    LaunchedEffect(Unit) {
        if (meal.value != null) {
            return@LaunchedEffect
        }

        if (name == null) {
            meal.value = Client.getRandomMeal()
        } else {
            meal.value = Client.getMealByName(name)
        }
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

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.TopCenter).padding(top=50.dp)) {
            Text(
                text = "${meal.value?.strMeal}",
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
        ) {

            if (imageBitmap.value != null) {
                Image(
                    bitmap = imageBitmap.value!!.asImageBitmap(),
                    contentDescription = "Random Recipe",
                    modifier =
                        Modifier
                            .padding(vertical = 10.dp)
                            .size(200.dp)
                            .clip(RoundedCornerShape(25.dp)),
                )
            }

            Button(onClick = {isRecipeDisplayed = !isRecipeDisplayed}) {
                Row {
                    Text("Recipe")

                    val imageSrc = when (isRecipeDisplayed) {
                        true -> "file:///android_asset/triangle-down.png"
                        false -> "file:///android_asset/triangle-right.png"
                    }

                    AsyncImage(
                        model = imageSrc,
                        contentDescription = "visual queue for showing the recipe",
                        modifier = Modifier.size(10.dp)
                    )
                }
            }

            for (i in 0..<meal.value?.ingredients!!.size) {
                val ingredient = meal.value?.ingredients!![i]
                val amount = meal.value?.measures!![i]
                Text("$ingredient, amount: $amount")
            }
        }

        Row(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 50.dp),
        ) {
            val buttonModifier =
                Modifier
                    .padding(horizontal = 10.dp)

            OutlinedButton(
                modifier = buttonModifier,
                onClick = { navController.navigate(AppRoutes.Home.route) }
            ) { Text("Back Home") }

            Button(
                modifier = buttonModifier,
                onClick = { navController.navigate(AppRoutes.Home.route) }
            ) { Text("add to fridge") }
        }

    }

}
