package com.sonnenstahl.recime.utils

import android.content.Context
import android.util.Log
import com.sonnenstahl.recime.utils.data.Meal
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

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
}
