package com.sonnenstahl.recime

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sonnenstahl.recime.utils.data.MEAT_OPTION_NAMES
import com.sonnenstahl.recime.utils.data.MeatOption
import com.sonnenstahl.recime.utils.data.disableAllButVegan
import com.sonnenstahl.recime.utils.data.disableMeat
import com.sonnenstahl.recime.utils.data.disableVegan
import com.sonnenstahl.recime.utils.data.excludedNames
import com.sonnenstahl.recime.utils.data.initMeatOptions
import com.sonnenstahl.recime.utils.data.veggies

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
                    .height(300.dp)
                    .weight(1f),
                userScrollEnabled = false,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(meatOptions.size) { index ->
                    val currentOption = meatOptions[index]
                    val isChosen = currentOption.isChosen.value

                    Log.d("OPTIONS", "${currentOption.name}: $isChosen")

                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()

                    val containerColor by animateColorAsState(
                        targetValue = when {
                            isChosen -> MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                            isPressed -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
                            else -> MaterialTheme.colorScheme.surface
                        },
                        animationSpec = spring(), label = "containerColorAnimation"
                    )

                    val contentColor by animateColorAsState(
                        targetValue = if (isChosen) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                        animationSpec = spring(), label = "contentColorAnimation"
                    )

                    val borderColor by animateColorAsState(
                        targetValue = if (isChosen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        animationSpec = spring(), label = "borderColorAnimation"
                    )

                    val elevation by animateDpAsState(
                        targetValue = if (isPressed) 4.dp else 0.dp,
                        animationSpec = spring(), label = "elevationAnimation"
                    )

                    if (isChosen) {
                        if (currentOption.name in excludedNames.take(2)) {
                            disableMeat(meatOptions)
                        }
                        val meats = MEAT_OPTION_NAMES
                            .take(MEAT_OPTION_NAMES.size )
                            .filter { it != "Pasta" && it != "Miscellaneous" && it !in veggies }

                        if (currentOption.name in meats) {
                            disableVegan(meatOptions)
                        }
                        if (currentOption.name == "Vegan") {
                            disableAllButVegan(meatOptions)
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            currentOption.isChosen.value = !isChosen
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(68.dp)
                            .padding(horizontal = 2.dp)
                            .padding(vertical = elevation),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(2.dp, borderColor),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = containerColor,
                            contentColor = contentColor
                        ),
                        interactionSource = interactionSource
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            val text =
                                if (currentOption.name != "Miscellaneous") currentOption.name
                                else "other"
                            Text(
                                text = text,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

        }
    }
}