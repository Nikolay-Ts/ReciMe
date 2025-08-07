package com.sonnenstahl.recime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.sonnenstahl.recime.utils.AppRoutes
import com.sonnenstahl.recime.utils.TempStorage
import com.sonnenstahl.recime.utils.data.Ingredient
import com.sonnenstahl.recime.utils.data.SearchType

/**
 * popup dialog use for [Fridge] and is used to display the item that has been double clicked
 *
 * @param navController to navigate to the recipes
 * @param ingredients to remove the current one if the user no longer wants/has it
 * @param ingredient the double tapped ingredient to be displayed
 * @param onDismiss what to do if the user taps off the popup display
 */
@Composable
fun IngredientPopupDialog(
    navController: NavController,
    ingredients: SnapshotStateList<Ingredient>,
    ingredient: Ingredient,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = null,
        text = {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AsyncImage(
                    model = ingredient.filePath,
                    contentDescription = ingredient.name,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                )

                Text(
                    text = ingredient.name,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 20.dp),
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Button(
                        onClick = {
                            ingredients.remove(ingredient)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1.0f),
                        shape = RoundedCornerShape(12.dp),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError,
                            ),
                    ) {
                        Text("Delete", modifier = Modifier.padding(end = 8.dp))

                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                        )
                    }

                    Button(
                        onClick = {
                            TempStorage.updateIngredients(listOf(ingredient.name))
                            val url = "${AppRoutes.Recipes.route}/${SearchType.INGREDIENTS}/${ingredient.name}"
                            navController.navigate(url)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1.0f),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text("Search", modifier = Modifier.padding(end = 8.dp))

                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                        )
                    }
                }
            }
        },
    )
}
