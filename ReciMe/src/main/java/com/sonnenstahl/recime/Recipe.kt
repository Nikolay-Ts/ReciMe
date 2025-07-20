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
import com.sonnenstahl.recime.utils.data.RandomMeal

@Composable
fun Recipe(name: String?) {
    val randomMeal = remember { mutableStateOf<RandomMeal?>(null) }
    val imageBitmap = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit) {
        if (name == null) {
            randomMeal.value = Client.getRandomMeal()
        } else {
            randomMeal.value = Client.getMealByName(name)
        }
    }

    LaunchedEffect(randomMeal.value) {
        imageBitmap.value =
            Client.getImage(
                mealName = "${randomMeal.value?.strMealThumb}",
                imageSize = ImageSize.LARGE,
            )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        if (randomMeal.value == null || imageBitmap.value == null) {
            Loading()
            return
        }

        Log.d("MEOW MEOW", "${randomMeal.value}")
        Text(
            text = "You should try\n ${randomMeal.value?.strMeal}!",
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

        for (i in 0..<randomMeal.value?.ingredients!!.size) {
            val ingredient = randomMeal.value?.ingredients!![i]
            val amount = randomMeal.value?.measures!![i]
            Text("$ingredient, amount: $amount")
        }
    }
}
