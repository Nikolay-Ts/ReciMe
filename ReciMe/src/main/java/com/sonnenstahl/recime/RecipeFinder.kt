package com.sonnenstahl.recime

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sonnenstahl.recime.utils.AppRoutes
import com.sonnenstahl.recime.utils.TempStorage
import com.sonnenstahl.recime.utils.data.EXCLUDED_NAMES
import com.sonnenstahl.recime.utils.data.MEAT_OPTION_NAMES
import com.sonnenstahl.recime.utils.data.MealOption
import com.sonnenstahl.recime.utils.data.VEGGIES
import com.sonnenstahl.recime.utils.data.disableAllButVegan
import com.sonnenstahl.recime.utils.data.disableMeat
import com.sonnenstahl.recime.utils.data.disableVegan
import com.sonnenstahl.recime.utils.data.initMealTypeOptions
import com.sonnenstahl.recime.utils.data.initMeatOptions

@Composable
fun RecipeFinder(navController: NavController) {
    var mealName by remember { mutableStateOf("") }
    var meatOptions by remember { mutableStateOf(emptyList<MealOption>()) }
    var mealTimeOptions by remember { mutableStateOf(emptyList<MealOption>()) }

    LaunchedEffect(Unit) {
        meatOptions = initMeatOptions()
        mealTimeOptions = initMealTypeOptions()
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Find a meal",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 10.dp),
            )

            TextField(
                value = mealName,
                singleLine = true,
                onValueChange = { mealName = it },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .padding(bottom = 20.dp),
            )

            OptionsGrid(
                options = meatOptions,
                columns = 3,
            ) { mealOption ->
                val isChosen = mealOption.isChosen.value
                if (isChosen) {
                    if (mealOption.name in EXCLUDED_NAMES.take(2)) {
                        disableMeat(meatOptions)
                    }
                    val meats =
                        MEAT_OPTION_NAMES
                            .take(MEAT_OPTION_NAMES.size)
                            .filter { it != "Pasta" && it != "Miscellaneous" && it !in VEGGIES }

                    if (mealOption.name in meats) {
                        disableVegan(meatOptions)
                    }
                    if (mealOption.name == "Vegan") {
                        disableAllButVegan(meatOptions)
                    }
                }
            }

            OptionsGrid(
                options = mealTimeOptions,
                columns = 3,
            ) { }
        }

        Button(
            onClick = {
                TempStorage.updateMeatOptions(meatOption = meatOptions)
                TempStorage.updateMealOptions(mealOption = mealTimeOptions)
                navController.navigate(AppRoutes.SearchRecipes.route)
              },
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 50.dp),
        ) {
            Text("Search")
        }
    }
}
