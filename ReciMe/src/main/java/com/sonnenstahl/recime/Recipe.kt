package com.sonnenstahl.recime

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.sonnenstahl.recime.utils.Client
import com.sonnenstahl.recime.utils.ImageSize
import com.sonnenstahl.recime.utils.TempStorage
import com.sonnenstahl.recime.utils.data.Meal

/**
 * Shows the current recipe passed through as a name. There is also a TempStorage
 * which caches the data and if the cache is sucesfull it will just load it in but if it fails
 * it fetches it again by name
 *
 * @param name the name of the meal
 */
@Composable
fun Recipe(name: String?) {
    val meal = remember { mutableStateOf<Meal?>(TempStorage.chosenMeal.value) }
    val imageBitmap = remember { mutableStateOf<Bitmap?>(TempStorage.chosenMealImg.value) }

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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        if (meal.value == null || imageBitmap.value == null) {
            Loading()
            return
        }

        Log.d("MEOW MEOW", "${meal.value}")
        Text(
            text = "You should try\n ${meal.value?.strMeal}!",
        )

        if (imageBitmap.value != null) {
            Image(
                bitmap = imageBitmap.value!!.asImageBitmap(),
                contentDescription = "Random Recipe",
                modifier =
                    Modifier
                        .size(200.dp),
            )
        }

        for (i in 0..<meal.value?.ingredients!!.size) {
            val ingredient = meal.value?.ingredients!![i]
            val amount = meal.value?.measures!![i]
            Text("$ingredient, amount: $amount")
        }
    }
}
