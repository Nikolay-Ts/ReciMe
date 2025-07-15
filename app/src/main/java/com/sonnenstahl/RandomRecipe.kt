package com.sonnenstahl

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sonnenstahl.recime.utils.Client
import com.sonnenstahl.recime.utils.data.RandomMeal
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

@Composable
fun RandomRecipe() {
    val randomMeal = remember { mutableStateOf<RandomMeal?>(null) }
    val imageBitmap = remember { mutableStateOf<Bitmap?>(null) }



    LaunchedEffect(Unit) {
        randomMeal.value = Client.getRandomRecipe()
    }

    LaunchedEffect(randomMeal.value) {
        imageBitmap.value = Client.getImage("${randomMeal.value?.strMealThumb}/preview")
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (randomMeal.value == null || imageBitmap.value == null) {
            Text("Loading")
            return
        }



        Text(
            text = "You should try\n ${randomMeal.value?.strMeal}!",
        )



        if (imageBitmap.value != null ) {
            Image(
                bitmap = imageBitmap.value!!.asImageBitmap(),
                contentDescription = "Random Recipe",
                modifier =
                    Modifier
                        .size(200.dp)
            )
        }

    }
}
