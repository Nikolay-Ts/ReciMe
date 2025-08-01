package com.sonnenstahl

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceModifier
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text

@Composable
fun DailyWidgetView() {
    Column(
        modifier =
            GlanceModifier
                .fillMaxSize()
                .background(Color.Red),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Hello World")
    }
}