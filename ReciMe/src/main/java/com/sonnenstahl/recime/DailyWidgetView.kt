package com.sonnenstahl.recime

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.ImageProvider
import androidx.glance.text.Text
import com.sonnenstahl.recime.utils.MealFileManager
import com.sonnenstahl.recime.utils.data.Meal
import java.io.File
import androidx.core.graphics.scale

@Composable
fun DailyWidgetView() {
    val context = LocalContext.current

    val meal: Meal? = MealFileManager.loadData(context).firstOrNull()
    val imageFile = File(context.cacheDir, MealFileManager.IMAGE_FILE_NAME)

    val bitmap = if (imageFile.exists()) BitmapFactory.decodeFile(imageFile.absolutePath) else null
    val resizedBitmap = bitmap?.scale(256, 256)

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (meal == null || resizedBitmap == null) {
            Text("Could not load meal.")
            return@Column
        }

        val icon = Icon.createWithBitmap(resizedBitmap)
        val imageProvider = ImageProvider(icon)

        Image(
            provider = imageProvider,
            contentDescription = meal.strMeal
        )
        Text(text = meal.strMeal)
    }
}