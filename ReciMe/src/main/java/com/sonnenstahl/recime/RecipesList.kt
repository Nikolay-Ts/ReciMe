package com.sonnenstahl.recime

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.sonnenstahl.recime.ui.theme.RecipesBackground
import com.sonnenstahl.recime.utils.data.Meal

/**
 * a lazy list that displays the recipes and allows for refreshing of them. And navigates
 * to the clicked recipe Users can also turn of the refreshing
 *
 * @param meals the lis tof meals you want to display
 * @param images that correspond to the images in 1:1 ratio
 * @param refreshing boolean to tell the lazy list to refresh
 * @param onRefresh sets the boolean to true. What actually happens to refresh is up to you
 * @param onClick where to navigate when clicking the meal
 */
@Composable
fun RecipesList(
    meals: List<Meal?>,
    images: List<Bitmap?>,
    refreshing: Boolean,
    onRefresh: () -> Unit,
    onClick: (Meal) -> Unit,
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshing),
        onRefresh = onRefresh,
    ) {
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(vertical = 50.dp)
                    .padding(horizontal = 30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Text(
                    text = "Recipes",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(vertical = 10.dp),
                )
            }

            items(meals.size) { index ->
                val meal = meals[index]
                val imageBitmap = images[index]
                if (meal == null) {
                    return@items
                }

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .padding(vertical = 10.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(RecipesBackground)
                            .clickable { onClick(meal) },
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            meal.strMeal,
                            color = Color.White,
                            maxLines = 2,
                            fontWeight = FontWeight.Bold,
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .padding(start = 16.dp),
                        )

                        if (imageBitmap != null) {
                            Image(
                                bitmap = imageBitmap.asImageBitmap(),
                                contentDescription = meal.strMeal,
                                modifier =
                                    Modifier
                                        .size(70.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                            )
                        }
                    }
                }
            }
        }
    }
}
