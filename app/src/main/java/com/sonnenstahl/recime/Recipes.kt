package com.sonnenstahl.recime

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sonnenstahl.recime.utils.AppRoutes
import com.sonnenstahl.recime.utils.Client
import com.sonnenstahl.recime.utils.data.RandomMeal

@Composable
fun Recipes(navController: NavController) {
    val randomMeal = remember { mutableStateOf<RandomMeal?>(null) }
    val imageBitmap = remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    LaunchedEffect(Unit) {
        randomMeal.value = Client.getRandomRecipe()
    }

    LaunchedEffect(randomMeal.value) {
        imageBitmap.value = Client.getImage("${randomMeal.value?.strMealThumb}")
    }

    if (randomMeal.value == null || imageBitmap.value == null) {
        Loading()
        return
    }

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Blue)
                        .clickable { navController.navigate(AppRoutes.Recipe.route) }
                ,
                contentAlignment = Alignment.Center
            ) {
                Row {
                    Image(
                        bitmap = imageBitmap.value!!.asImageBitmap(),
                        contentDescription = "meow Meow"
                    )

                    Text(
                        "Hello World",
                        color = Color.White
                    )
                }

            }

        }
    }





    Text("Hello World")
}