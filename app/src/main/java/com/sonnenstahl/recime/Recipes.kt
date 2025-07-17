package com.sonnenstahl.recime

import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.Bitmap
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.sonnenstahl.recime.ui.theme.RecipesBackground
import com.sonnenstahl.recime.utils.AppRoutes
import com.sonnenstahl.recime.utils.Client
import com.sonnenstahl.recime.utils.ImageSize
import com.sonnenstahl.recime.utils.data.RandomMeal

@Composable
fun Recipes(navController: NavController) {
    val randomMeals = remember { mutableStateListOf<RandomMeal?>() }
    val images = remember { mutableStateListOf<Bitmap?>() }
    var refreshing by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        Client.getRandomMeals(randomMeals)
    }

    LaunchedEffect(randomMeals.size) {
        for (meal in randomMeals) {
            images.add(Client.getImage(
                mealName = meal?.strMealThumb ?: "",
                imageSize = ImageSize.SMALL
            ))
        }
        refreshing = false
    }

    LaunchedEffect(refreshing) {
        if (refreshing) {
            randomMeals.clear()
            images.clear()

            Client.getRandomMeals(randomMeals)
        }
    }

    if (randomMeals.size != images.size && randomMeals.isNotEmpty()) {
        Loading()
        return
    }
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshing),
        onRefresh = { refreshing = true },
    ) {
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(vertical = 50.dp)
                    .padding(horizontal = 30.dp),
            verticalArrangement = Arrangement.Center
        ) {
            items(randomMeals.size) { index ->
                val meal = randomMeals[index]
                val imageBitmap = images[index]

                if (meal == null) {
                    return@items
                }

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .padding(vertical = 10.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(RecipesBackground)
                            .clickable {
                                navController.navigate("${AppRoutes.Recipe.route}/${meal.strMeal}")
                            },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            meal.strMeal,
                            color = Color.White,
                            maxLines = 2,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp)
                        )

                        if (imageBitmap != null) {
                            Image(
                                bitmap = imageBitmap.asImageBitmap(),
                                contentDescription = meal.strMeal,
                                modifier =
                                    Modifier
                                        .size(70.dp)
                                        .clip(RoundedCornerShape(12.dp))
                            )
                        }
                    }
                }
            }


        }
    }
}