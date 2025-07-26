package com.sonnenstahl.recime

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.sonnenstahl.recime.utils.AppRoutes
import com.sonnenstahl.recime.utils.TempStorage
import com.sonnenstahl.recime.utils.data.Ingredient
import com.sonnenstahl.recime.utils.data.SearchType
import com.sonnenstahl.recime.utils.data.TEST_VEGGIES_LIST

@Composable
fun Fridge(navController: NavController) {
    val ingredients = remember { mutableStateListOf<Ingredient>() }
    var isSelectingMany by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        ingredients.addAll(TEST_VEGGIES_LIST)
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(top=16.dp)
                .padding(horizontal = 16.dp)
                .clickable(
                    enabled = isSelectingMany,
                    indication = null,
                    interactionSource = null,
                    onClick = {
                        isSelectingMany = false
                        ingredients.forEach { it.isSelected.value = false }
                    },
                ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
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
                contentPadding =
                    androidx.compose.foundation.layout
                        .PaddingValues(8.dp),
            ) {
                items(
                    items = ingredients,
                    key = { ingredient: Ingredient -> ingredient.id },
                ) { ingredient ->
                    val infiniteTransition = rememberInfiniteTransition(label = "shakingTransition")
                    val rotationAngle by infiniteTransition.animateFloat(
                        initialValue = -0.25f, // rotation
                        targetValue = 0.25f,
                        animationSpec =
                            infiniteRepeatable(
                                animation = tween(durationMillis = 100),
                                repeatMode = RepeatMode.Reverse,
                            ),
                        label = "rotationAngle",
                    )

                    Box(modifier = Modifier.fillMaxSize()) {
                        Card(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(4.dp)
                                    .graphicsLayer(rotationZ = if (isSelectingMany) rotationAngle else 0f)
                                    .combinedClickable(
                                        onClick = {
                                            if (isSelectingMany) {
                                                ingredient.isSelected.value = !ingredient.isSelected.value
                                            }
                                        },
                                        onLongClick = {
                                            isSelectingMany = true
                                            ingredient.isSelected.value = !ingredient.isSelected.value
                                        },
                                    ),
                            colors =
                                CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                ),
                            elevation =
                                CardDefaults.cardElevation(
                                    pressedElevation = 10.dp,
                                ),
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                            ) {
                                AsyncImage(
                                    model = ingredient.filePath,
                                    contentDescription = ingredient.name,
                                    modifier = Modifier.height(80.dp),
                                )
                                Text(ingredient.name, style = MaterialTheme.typography.bodySmall)
                            }
                        }

                        if (isSelectingMany) {
                            Box(
                                modifier =
                                    Modifier
                                        .align(Alignment.TopStart)
                                        .offset(x = (-4).dp, y = (-4).dp)
                                        .size(24.dp)
                                        .background(
                                            color = if (ingredient.isSelected.value) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant,
                                            shape = CircleShape,
                                        ).border(
                                            width = 2.dp,
                                            color = if (ingredient.isSelected.value) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant,
                                            shape = CircleShape,
                                        ),
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                if (isSelectingMany) {
                    isSelectingMany = false
                    ingredients.forEach { it.isSelected.value = false }
                } else {
                    navController.navigate(AppRoutes.Home.route)
                }
            },
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 16.dp, end = 16.dp),
        ) {
            Text(if (isSelectingMany) "Done" else "+")
        }

        Button(
            onClick = {
                val selectedIngredients = ingredients
                    .filter { it.isSelected.value == true }
                    .map    { it -> it.name.trim().replace(" ", "_") }
                TempStorage.updateIngredients(selectedIngredients)
                val searchType = if (selectedIngredients.isNotEmpty()) {SearchType.INGREDIENTS} else SearchType.NONE
                val url = "${AppRoutes.Recipes.route}/${searchType}/"
                navController.navigate(url)
            },
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 100.dp)
                    .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text("Search")
        }
    }
}
