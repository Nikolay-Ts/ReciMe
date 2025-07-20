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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonnenstahl.recime.utils.data.MealOption

@Composable
fun OptionsGrid(
    options: List<MealOption>,
    columns: Int,
    onclick: (MealOption) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier =
            Modifier
                .fillMaxWidth()
                .height(300.dp),
        userScrollEnabled = false,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(options.size) { index ->
            val currentOption = options[index]
            val isChosen = currentOption.isChosen.value

            Log.d("OPTIONS", "${currentOption.name}: $isChosen")

            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()

            val containerColor by animateColorAsState(
                targetValue =
                    when {
                        isChosen -> MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                        isPressed -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
                        else -> MaterialTheme.colorScheme.surface
                    },
                animationSpec = spring(),
                label = "containerColorAnimation",
            )

            val contentColor by animateColorAsState(
                targetValue = if (isChosen) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                animationSpec = spring(),
                label = "contentColorAnimation",
            )

            val borderColor by animateColorAsState(
                targetValue = if (isChosen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                animationSpec = spring(),
                label = "borderColorAnimation",
            )

            val elevation by animateDpAsState(
                targetValue = if (isPressed) 4.dp else 0.dp,
                animationSpec = spring(),
                label = "elevationAnimation",
            )

            onclick(currentOption)

            OutlinedButton(
                onClick = {
                    currentOption.isChosen.value = !isChosen
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(68.dp)
                        .padding(horizontal = 2.dp)
                        .padding(vertical = elevation),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(2.dp, borderColor),
                colors =
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = containerColor,
                        contentColor = contentColor,
                    ),
                interactionSource = interactionSource,
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    val text =
                        if (currentOption.name != "Miscellaneous") {
                            currentOption.name
                        } else {
                            "other"
                        }
                    Text(
                        text = text,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}
