package com.sonnenstahl.recime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.sonnenstahl.recime.utils.AppRoutes

@Composable
fun Home(navigation: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Hello World")

        Button(
            onClick = { navigation.navigate(AppRoutes.RandomRecipe.route) }
        ) {
            Text("Go to random reciper")
        }
    }
}
