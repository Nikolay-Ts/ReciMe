package com.sonnenstahl.recime.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.sonnenstahl.recime.utils.data.Meal
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

/**
 * used to save and load the daily image to prevent loading screen when entering the app
 * for a nicer user expirience
 */
object MealFileManager : FileManager<Meal> {
    override val FILENAME = "meals.json"
    private val json =
        Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    private const val IMAGE_FILE_NAME = "daily_meal.png"

    override fun saveData(
        context: Context,
        meal: List<Meal>,
    ) {
        try {
            val jsonString = json.encodeToString(meal)
            context.openFileOutput(FILENAME, Context.MODE_PRIVATE).use { outputStream ->
                outputStream.write(jsonString.toByteArray())
            }
        } catch (e: Exception) {
            Log.e("Saving Ingredients", e.toString())
        }
    }

    override fun loadData(context: Context): List<Meal> =
        try {
            context.openFileInput(FILENAME).use { inputStream ->
                val jsonString = inputStream.bufferedReader().use { it.readText() }
                json.decodeFromString<List<Meal>>(jsonString)
            }
        } catch (e: FileNotFoundException) {
            emptyList()
        } catch (e: Exception) {
            emptyList()
        }

    override fun deleteData(context: Context) =
        {
            try {
                val file = File(context.filesDir, FILENAME)
                if (file.exists()) {
                    val deleted = file.delete()
                    deleteMealBitmap(context)
                    if (deleted) {
                        println("$FILENAME deleted successfully.")
                    } else {
                        System.err.println("Failed to delete ${MealFileManager.FILENAME}.")
                    }
                } else {
                    println("$FILENAME does not exist.")
                }
            } catch (e: Exception) {
                System.err.println("Error deleting meal file: ${e.message}")
                e.printStackTrace()
            }
        }

    fun loadMealBitmap(context: Context): Bitmap? {
        val file = File(context.cacheDir, IMAGE_FILE_NAME)
        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    }


    fun saveMealBitmap(context: Context, bitmap: Bitmap?) {
        bitmap ?: return

        val directory = File(context.cacheDir, IMAGE_FILE_NAME)
        if (!directory.exists()) directory.mkdirs()

        val file = File(directory, IMAGE_FILE_NAME)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
        }
    }

    private fun deleteMealBitmap(context: Context): Boolean {
        val file = File(context.cacheDir, IMAGE_FILE_NAME)
        return file.delete()
    }
}
