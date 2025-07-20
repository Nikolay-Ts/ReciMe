package com.sonnenstahl.recime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sonnenstahl.recime.utils.AppRoutes

@Composable
fun Home(navigation: NavHostController) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(top = 50.dp)
                .padding(horizontal = 30.dp),
    ) {
        Text(
            "meow meow",
            modifier =
                Modifier
                    .align(Alignment.TopStart),
        )

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
            onClick = { navigation.navigate(AppRoutes.Recipes.route) },
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 50.dp),
        ) {
            Text("Surprise")
        }
    }
}
