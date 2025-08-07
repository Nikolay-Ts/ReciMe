package com.sonnenstahl.recime

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.sonnenstahl.recime.utils.IngredientFileManager
import com.sonnenstahl.recime.utils.data.Ingredient
import kotlinx.coroutines.launch

/**
 * this will display the current groceries plus will display new groceries that the user
 * wants to add
 *
 * @param navController to navigate back if need (todo: see if this is needed)
 */
@Composable
fun AddIngredients(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var newIngredientName by remember { mutableStateOf("") }
    val allIngredients = remember { mutableStateListOf<Ingredient>() }

    LaunchedEffect(Unit) {
        val loadedList = IngredientFileManager.loadData(context)
        allIngredients.addAll(loadedList)
    }

    val filteredIngredients =
        remember(newIngredientName, allIngredients) {
            if (newIngredientName.isBlank()) {
                allIngredients
            } else {
                allIngredients.filter {
                    it.name.contains(newIngredientName, ignoreCase = true)
                }
            }
        }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(top = 25.dp)
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = "Add New Ingredient",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        OutlinedTextField(
            value = newIngredientName,
            onValueChange = { newIngredientName = it },
            label = { Text("Ingredient Name (Type to Filter)") }, // Updated label
            singleLine = true,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
        )

        Button(
            onClick = {
                if (newIngredientName.isNotBlank()) {
                    val trimmedName = newIngredientName.trim()
                    if (allIngredients.none { it.name.equals(trimmedName, ignoreCase = true) }) {
                        val newIngredient = Ingredient(name = trimmedName)
                        allIngredients.add(newIngredient)

                        coroutineScope.launch {
                            IngredientFileManager.saveData(context, allIngredients.toList())
                            Toast.makeText(context, "$trimmedName added!", Toast.LENGTH_SHORT).show()
                        }
                        newIngredientName = ""
                    } else {
                        Toast.makeText(context, "$trimmedName already exists!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Ingredient name cannot be empty.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
        ) {
            Text("Add Ingredient")
        }

        Text(
            text = "Existing Ingredients:",
            style = MaterialTheme.typography.titleMedium,
            modifier =
                Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp),
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding =
                androidx.compose.foundation.layout
                    .PaddingValues(8.dp),
        ) {
            items(
                items = filteredIngredients,
                key = { ingredient: Ingredient -> ingredient.id },
            ) { ingredient ->
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                ) {
                    Card(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(4.dp),
                        colors =
                            CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            ),
                        elevation = CardDefaults.cardElevation(4.dp),
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
                            Text(
                                text = ingredient.name,
                                style = MaterialTheme.typography.bodySmall,
                                softWrap = true,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        }
    }
}
