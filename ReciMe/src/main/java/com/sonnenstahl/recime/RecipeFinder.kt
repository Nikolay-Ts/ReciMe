package com.sonnenstahl.recime

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.sonnenstahl.recime.utils.data.SearchType
import com.sonnenstahl.recime.utils.data.VEGGIES
import com.sonnenstahl.recime.utils.data.disableAllButVegan
import com.sonnenstahl.recime.utils.data.disableMeat
import com.sonnenstahl.recime.utils.data.disableVegan
import com.sonnenstahl.recime.utils.data.initMealTypeOptions
import com.sonnenstahl.recime.utils.data.initMeatOptions

/**
 * This view allws the use to choose by name a dish they had seen before or think exists. Also this
 * view allows the user to add restrictions or preferences such as vegan, seafood only, along side
 * if they a main dish, breakfast...
 *
 * if the user choses nothing and continues to [Recipes] the [Meal]s are chosen at random
 *
 * @param navController to navigate between views
 */
@Composable
fun RecipeFinder(navController: NavController) {
    var mealName by remember { mutableStateOf("") }
    var meatOptions by remember { mutableStateOf(emptyList<MealOption>()) }
    var mealTimeOptions by remember { mutableStateOf(emptyList<MealOption>()) }

    LaunchedEffect(Unit) {
        meatOptions = initMeatOptions()
        mealTimeOptions = initMealTypeOptions()
        TempStorage.clearSuggestedMeals()
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
                    .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Find a meal",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 24.dp),
            )

            OutlinedTextField(
                value = mealName,
                singleLine = true,
                onValueChange = { mealName = it },
                label = { Text("Search by meal name") },
                shape = RoundedCornerShape(12.dp),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
            )

            Text(
                text = "Dietary Preferences",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp),
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

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Meal Type",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp),
            )
            OptionsGrid(
                options = mealTimeOptions,
                columns = 3,
            ) { }
        }

        Button(
            onClick = {
                TempStorage.updateMeatOptions(meatOption = meatOptions)
                TempStorage.updateMealOptions(mealOption = mealTimeOptions)
                val urlName = if (mealName == "") null else mealName
                val searchType =
                    when (
                        mealTimeOptions.any { it.isChosen.value } ||
                            meatOptions.any { it.isChosen.value }
                    ) {
                        true -> SearchType.CATEGORY
                        false -> SearchType.NONE
                    }

                val url = "${AppRoutes.Recipes.route}/$searchType/$urlName"
                navController.navigate(url)
            },
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text("Search")
        }
    }
}
