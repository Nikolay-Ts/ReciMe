package com.sonnenstahl.recime

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sonnenstahl.recime.utils.AppRoutes
import com.sonnenstahl.recime.utils.Client
import com.sonnenstahl.recime.utils.ImageSize
import com.sonnenstahl.recime.utils.MealFileManager
import com.sonnenstahl.recime.utils.TempStorage
import com.sonnenstahl.recime.utils.data.Meal
import com.sonnenstahl.recime.utils.data.NO_INTERNET_CONNECTION
import com.sonnenstahl.recime.utils.data.SearchType
import com.sonnenstahl.recime.utils.isConnectedToInternet
import com.sonnenstahl.recime.utils.vibrateAndShowToast
import kotlinx.coroutines.delay

@Composable
fun Home(navigation: NavHostController) {
    val context = LocalContext.current
    var meal by remember { mutableStateOf<Meal?>(null) }
    var mealImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isConnectedToInternet by remember { mutableStateOf(isConnectedToInternet(context)) }

    LaunchedEffect(Unit) {
        TempStorage.clearSuggestedMeals()
        val loadedMeals = MealFileManager.loadData(context)
        meal = loadedMeals.firstOrNull()
        if (meal == null && isConnectedToInternet) {
            val randomMeal = Client.getRandomMeal()
            meal = randomMeal
            if (randomMeal != null) {
                MealFileManager.saveData(context, listOf(randomMeal))
            }
        }
        meal?.let {
            mealImageBitmap = Client.getImage(it.strMealThumb ?: "", ImageSize.LARGE)
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            isConnectedToInternet = isConnectedToInternet(context)
            delay(1000L)
        }
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
                    .padding(start = 5.dp, end = 5.dp)
                    .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(
                onClick = { navigation.navigate(AppRoutes.Fridge.route) },
                shape = RoundedCornerShape(20.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
            }

            Button(
                onClick = {
                    if (!isConnectedToInternet) {
                        vibrateAndShowToast(
                            context,
                            NO_INTERNET_CONNECTION,
                            500L,
                        )
                        return@Button
                    }

                    navigation.navigate(AppRoutes.RecipeFinder.route)
                },
                shape = RoundedCornerShape(20.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
            }
        }

        // meal of the day
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp),
            ) {
                if (!isConnectedToInternet) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth()
                            .height(200.dp),
                    ) {
                        val bitmap =
                            remember {
                                val inputStream = context.assets.open("no-wifi.png")
                                BitmapFactory.decodeStream(inputStream)
                            }

                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Take Picture",
                            modifier = Modifier.size(60.dp),
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "No Internet Connection",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Please check your Wi-Fi or mobile data and try again.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }

                    return@Card
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                        Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                ) {
                    Text(
                        text = "Daily Meal",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )

                    if (meal != null && mealImageBitmap != null) {
                        Text(
                            text = meal!!.strMeal,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp),
                        )
                        Image(
                            bitmap = mealImageBitmap!!.asImageBitmap(),
                            contentDescription = meal!!.strMeal,
                            modifier =
                                Modifier
                                    .size(240.dp)
                                    .clip(RoundedCornerShape(25.dp)),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                TempStorage.updateChosenMeal(meal)
                                TempStorage.updateChosenMealImage(mealImageBitmap)
                                val url = "${AppRoutes.Recipe.route}/${meal!!.strMeal}"
                                navigation.navigate(url)
                            },
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Text("More Info")
                        }
                    } else {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(400.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Loading()
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                if (!isConnectedToInternet) {
                    vibrateAndShowToast(
                        context,
                        NO_INTERNET_CONNECTION,
                        500L,
                    )
                    return@Button
                }

                val url = "${AppRoutes.Recipes.route}/${SearchType.NONE}/"
                navigation.navigate(url)
            },
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 36.dp),
            shape = RoundedCornerShape(24.dp),
        ) {
            Text("Surprise Me")
        }
    }
}
