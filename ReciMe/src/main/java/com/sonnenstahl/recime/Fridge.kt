package com.sonnenstahl.recime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.sonnenstahl.recime.utils.AppRoutes
import com.sonnenstahl.recime.utils.data.TEST_VEGGIES_LIST
import com.sonnenstahl.recime.utils.data.Ingredient

@Composable
fun Fridge(navController: NavController) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Fridge",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(bottom = 16.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(8.dp)
            ) {
                items(
                    items = TEST_VEGGIES_LIST,
                    key = { ingredient: Ingredient -> ingredient.id }
                ) { ingredient ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        AsyncImage(
                            model = ingredient.filePath,
                            contentDescription = ingredient.name,
                            modifier = Modifier.height(80.dp)
                        )
                        Text(ingredient.name, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        Button(
            onClick = { navController.navigate(AppRoutes.Home.route) },
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 16.dp, end = 16.dp),
        ) {
            Text("+")
        }
    }
}