package com.sonnenstahl.recime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sonnenstahl.recime.utils.AppRoutes
import com.sonnenstahl.recime.utils.TempStorage
import com.sonnenstahl.recime.utils.data.SearchType

@Composable
fun Home(navigation: NavHostController) {
    LaunchedEffect(Unit) {
        TempStorage.clearSuggestedMeals()
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(top = 50.dp)
                .padding(horizontal = 30.dp),
    ) {
        Button(
            onClick = { navigation.navigate(AppRoutes.Fridge.route) },
            modifier =
                Modifier
                    .align(Alignment.TopStart),
        ) {
            Text(
                "Fridge",
            )
        }

        Button(
            onClick = { navigation.navigate(AppRoutes.RecipeFinder.route) },
            modifier =
                Modifier
                    .align(Alignment.TopEnd),
        ) {
            Text(
                "got to recipe Finder",
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Text("Hello World")
        }

        Button(
            onClick = {
                val url = "${AppRoutes.Recipes.route}/${SearchType.NONE}/"
                navigation.navigate(url)
            },
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 50.dp),
        ) {
            Text("Surprise")
        }
    }
}
