package com.sonnenstahl.recime

import android.content.Context
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import com.sonnenstahl.DailyWidgetView

/**
 * receiver used to load the widget in the manifest
 */
class DailyWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = DailyWidget()
}

/**
 * the actual widget but it will load the composable function instead
 */
class DailyWidget : GlanceAppWidget() {
    companion object {
        private val TWO_BY_THREE = DpSize(140.dp, 210.dp)
    }

    override val sizeMode = SizeMode.Responsive(
        setOf(TWO_BY_THREE)
    )


    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        provideContent {
            DailyWidgetView()
        }
    }
}