package com.sonnenstahl.recime

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.Bitmap
import com.sonnenstahl.recime.utils.Client
import com.sonnenstahl.recime.utils.TempStorage
import com.sonnenstahl.recime.utils.data.Meal

@Composable
fun SearchRecipes(navController: NavController) {
    val mealByName = TempStorage.chosenMeal.collectAsStateWithLifecycle()
    val mealbyNameImage = TempStorage.chosenMeal.collectAsStateWithLifecycle()
    val meals = remember { mutableStateListOf<Meal?>() }
    val images = remember { mutableStateListOf<Bitmap?>() }

    LaunchedEffect(Unit) {

    }


    Box(modifier = Modifier.fillMaxSize()) {
        Text("Hello World")
    }
}