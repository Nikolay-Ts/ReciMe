package com.sonnenstahl.recime

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sonnenstahl.recime.utils.data.MEAT_OPTION_NAMES
import com.sonnenstahl.recime.utils.data.MeatOption
import com.sonnenstahl.recime.utils.data.disableMeat
import com.sonnenstahl.recime.utils.data.disableVegan
import com.sonnenstahl.recime.utils.data.excludedNames
import com.sonnenstahl.recime.utils.data.initMeatOptions

@Composable
fun RecipeFinder(navController: NavController) {
    var mealName by remember { mutableStateOf("") }
    var meatOptions by remember { mutableStateOf(emptyList<MeatOption>()) }

    LaunchedEffect(Unit) {
        meatOptions = initMeatOptions()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Log.d("MEAT OPTIONS", "$meatOptions")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Find a meal",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 10.dp)
            )

            TextField(
                value = mealName,
                singleLine = true,
                onValueChange = { mealName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                userScrollEnabled = false
            ) {
                items(meatOptions.size) { index ->
                    val currentOption = meatOptions[index]
                    val isChosen = meatOptions[index].isChosen.value

                    if (isChosen == true ) {
                        if (currentOption.name in excludedNames.take(2)) {
                            disableMeat(meatOptions)
                        }
                        // only the meats
                        val meats = MEAT_OPTION_NAMES
                            .take(MEAT_OPTION_NAMES.size-2)
                            .filter { it != "Pasta" }

                        if (currentOption.name in meats) {
                            disableVegan(meatOptions)
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            meatOptions[index].isChosen.value = !isChosen
                        },
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isChosen) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface,
                            contentColor = if (isChosen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text(
                            text = currentOption.name,
                            modifier = Modifier.padding(8.dp),
                            fontSize = 15.sp

                        )
                    }
                }
            }
        }
    }
}