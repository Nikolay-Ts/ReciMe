package com.sonnenstahl.recime

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.sonnenstahl.recime.utils.AppRoutes
import com.sonnenstahl.recime.utils.IngredientFileManager
import com.sonnenstahl.recime.utils.TempStorage
import com.sonnenstahl.recime.utils.data.Ingredient
import com.sonnenstahl.recime.utils.data.SearchType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NoRippleInteractionSource : MutableInteractionSource {
    override val interactions: kotlinx.coroutines.flow.Flow<Interaction> = kotlinx.coroutines.flow.emptyFlow()

    override suspend fun emit(interaction: Interaction) = Unit

    override fun tryEmit(interaction: Interaction): Boolean = true
}

@Composable
fun Fridge(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val ingredients = remember { mutableStateListOf<Ingredient>() }
    var isSelectingMany by remember { mutableStateOf(false) }
    val searchOffset = remember { Animatable(0f) }
    val deleteOffset = remember { Animatable(0f) }
    val pendingRemoval = remember { mutableStateListOf<Ingredient>() }

    var displayDialog by remember { mutableStateOf(false) }
    var ingredientDialog by remember { mutableStateOf<Ingredient>(Ingredient(name="")) }

    var deleteAllDialog by remember { mutableStateOf(false) }

    LaunchedEffect(navController.currentBackStackEntry) {
        TempStorage.clearSuggestedMeals()
        val loadedList = IngredientFileManager.loadData(context)
        ingredients.clear()
        ingredients.addAll(loadedList)
    }

    LaunchedEffect(isSelectingMany) {
        if (isSelectingMany) {
            searchOffset.animateTo(-64f, animationSpec = tween(300, easing = FastOutSlowInEasing))
            deleteOffset.animateTo(-128f, animationSpec = tween(300, easing = FastOutSlowInEasing))
        } else {
            searchOffset.animateTo(0f, animationSpec = tween(300, easing = FastOutSlowInEasing))
            deleteOffset.animateTo(0f, animationSpec = tween(300, easing = FastOutSlowInEasing))
        }
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
                .clickable(
                    enabled = isSelectingMany,
                    indication = null,
                    interactionSource = remember { NoRippleInteractionSource() },
                    onClick = {
                        if (isSelectingMany) {
                            isSelectingMany = false
                            ingredients.forEach { it.isSelected.value = false }
                            coroutineScope.launch {
                                IngredientFileManager.saveData(context, ingredients.toList())
                            }
                        }
                    },
                ),
    ) {
        if (displayDialog) {
            IngredientPopupDialog(
                navController = navController,
                ingredients = ingredients,
                ingredient = ingredientDialog
            ) { displayDialog = false }
        }

        if (deleteAllDialog) {
            DeleteAllDialog(ingredients = ingredients) {
                deleteAllDialog = false
                isSelectingMany = false
            }
        }

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
                modifier = Modifier.weight(1f).padding(bottom = 16.dp),
                contentPadding =
                    androidx.compose.foundation.layout
                        .PaddingValues(8.dp),
            ) {
                items(
                    items = ingredients,
                    key = { it.id },
                ) { ingredient ->
                    val infiniteTransition = rememberInfiniteTransition(label = "shakingTransition")
                    val rotationAngle by infiniteTransition.animateFloat(
                        initialValue = -0.25f,
                        targetValue = 0.25f,
                        animationSpec =
                            infiniteRepeatable(
                                animation = tween(durationMillis = 100),
                                repeatMode = RepeatMode.Reverse,
                            ),
                        label = "rotationAngle",
                    )

                    val visible = remember(ingredient.id) { mutableStateOf(true) }
                    val scale = remember(ingredient.id) { Animatable(1f) }
                    val alpha = remember(ingredient.id) { Animatable(1f) }

                    if (!visible.value) return@items

                    LaunchedEffect(pendingRemoval.contains(ingredient)) {
                        if (pendingRemoval.contains(ingredient)) {
                            scale.animateTo(0f, animationSpec = tween(300))
                            alpha.animateTo(0f, animationSpec = tween(300))
                            delay(50)
                            ingredients.remove(ingredient)
                            pendingRemoval.remove(ingredient)
                        }
                    }

                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .graphicsLayer(
                                    scaleX = scale.value,
                                    scaleY = scale.value,
                                    alpha = alpha.value,
                                ),
                    ) {
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
                                            isSelectingMany = !isSelectingMany
                                            ingredient.isSelected.value = !ingredient.isSelected.value
                                        },
                                        onDoubleClick = {
                                            displayDialog = true
                                            ingredientDialog = ingredient
                                        }
                                    ),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                            elevation = CardDefaults.cardElevation(pressedElevation = 10.dp),
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize().padding(8.dp),
                            ) {
                                AsyncImage(
                                    model = ingredient.filePath,
                                    contentDescription = ingredient.name,
                                    modifier = Modifier.height(80.dp),
                                )
                                Text(
                                    text = ingredient.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    softWrap = true,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
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

        Box(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 16.dp, end = 16.dp)
                    .size(56.dp)
                    .combinedClickable(
                        onClick = {
                            if (isSelectingMany) {
                                isSelectingMany = false
                                ingredients.forEach { it.isSelected.value = false }
                                coroutineScope.launch {
                                    IngredientFileManager.saveData(context, ingredients.toList())
                                }
                            } else {
                                coroutineScope.launch {
                                    IngredientFileManager.saveData(context, ingredients.toList())
                                }
                                navController.navigate(AppRoutes.Home.route)
                            }
                        },
                        onLongClick = {
                            isSelectingMany = !isSelectingMany
                        },
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ),
        ) {
            val fabScale by animateFloatAsState(
                targetValue = if (isSelectingMany) 1f else 0f,
                animationSpec = tween(300),
                label = "fabScale",
            )
            val fabAlpha by animateFloatAsState(
                targetValue = if (isSelectingMany) 1f else 0f,
                animationSpec = tween(300),
                label = "fabAlpha",
            )

            val fabSpacing = 56.dp

            val searchOffset by animateDpAsState(
                targetValue = if (isSelectingMany) fabSpacing * 2 else 0.dp,
                animationSpec = tween(300),
                label = "searchOffset",
            )

            val deleteOffset by animateDpAsState(
                targetValue = if (isSelectingMany) fabSpacing else 0.dp,
                animationSpec = tween(300),
                label = "deleteOffset",
            )

            SmallFloatingActionButton(
                onClick = {
                    if (ingredients.all { it.isSelected.value == false }) {
                        deleteAllDialog = true
                        return@SmallFloatingActionButton
                    }

                    val toDelete = ingredients.filter { it.isSelected.value }
                    pendingRemoval.addAll(toDelete)
                    coroutineScope.launch {
                        IngredientFileManager.saveData(
                            context,
                            ingredients.toList().filterNot { toDelete.contains(it) },
                        )
                    }
                    isSelectingMany = false
                    ingredients.forEach { it.isSelected.value = false }
                },
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .offset(y = -deleteOffset)
                        .graphicsLayer {
                            scaleX = fabScale
                            scaleY = fabScale
                            alpha = fabAlpha
                        }.padding(end = 16.dp, bottom = 16.dp)
                        .size(48.dp),
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete Selected")
            }

            SmallFloatingActionButton(
                onClick = {
                    val selected = ingredients.filter { it.isSelected.value }.map { it.name.trim().replace(" ", "_") }
                    TempStorage.updateIngredients(selected)
                    val searchType = if (selected.isNotEmpty()) SearchType.INGREDIENTS else SearchType.NONE
                    val url = "${AppRoutes.Recipes.route}/$searchType/"
                    isSelectingMany = false
                    ingredients.forEach { it.isSelected.value = false }
                    navController.navigate(url)
                },
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .offset(y = -searchOffset)
                        .graphicsLayer {
                            scaleX = fabScale
                            scaleY = fabScale
                            alpha = fabAlpha
                        }.padding(end = 16.dp, bottom = 16.dp)
                        .size(48.dp),
            ) {
                Icon(Icons.Filled.Search, contentDescription = "Search for Recipes")
            }

            FloatingActionButton(
                onClick = {
                    if (isSelectingMany) {
                        isSelectingMany = false
                        ingredients.forEach { it.isSelected.value = false }
                        coroutineScope.launch {
                            IngredientFileManager.saveData(context, ingredients.toList())
                        }
                    } else {
                        coroutineScope.launch {
                            IngredientFileManager.saveData(context, ingredients.toList())
                        }
                        navController.navigate(AppRoutes.Home.route)
                    }
                },
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(
                    imageVector = if (isSelectingMany) Icons.Filled.Done else Icons.Filled.Add,
                    contentDescription = if (isSelectingMany) "Done" else "Add New",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}
