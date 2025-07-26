package com.sonnenstahl.recime.utils

import android.content.Context
import android.util.Log
import com.sonnenstahl.recime.utils.data.Ingredient
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

/**
 * object to load, store and delete the ingredients in your fridge
 */
object IngredientFileManager {
    private const val FILENAME = "ingredients.json"
    private val json =
        Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }

    /**
     * Saves a list of ingredients to internal storage as a JSON file.
     *
     * @param context The application context.
     * @param ingredients The list of [Ingredient] objects to save.
     */
    fun saveIngredients(
        context: Context,
        ingredients: List<Ingredient>,
    ) = try {
        val jsonString = json.encodeToString(ingredients)
        context.openFileOutput(FILENAME, Context.MODE_PRIVATE).use { outputStream ->
            outputStream.write(jsonString.toByteArray())
        }
    } catch (e: Exception) {
        Log.e("Saving Ingredients", e.toString())
    }

    /**
     * loads a list of ingredients from a JSON file in internal storage.
     *
     * @param context The application context.
     * @return The loaded list of [Ingredient] objects, or an empty list if loading fails or file not found.
     */
    fun loadIngredients(context: Context): List<Ingredient> =
        try {
            context.openFileInput(FILENAME).use { inputStream ->
                val jsonString = inputStream.bufferedReader().use { it.readText() }
                json.decodeFromString<List<Ingredient>>(jsonString)
            }
        } catch (e: FileNotFoundException) {
            emptyList()
        } catch (e: Exception) {
            emptyList()
        }

    /**
     * Deletes the ingredients file from internal storage.
     * Use with caution!
     * @param context The application context.
     */
    fun deleteIngredientsFile(context: Context) =
        {
            try {
                val file = File(context.filesDir, FILENAME)
                if (file.exists()) {
                    val deleted = file.delete()
                    if (deleted) {
                        println("$FILENAME deleted successfully.")
                    } else {
                        System.err.println("Failed to delete $FILENAME.")
                    }
                } else {
                    println("$FILENAME does not exist.")
                }
            } catch (e: Exception) {
                System.err.println("Error deleting ingredients file: ${e.message}")
                e.printStackTrace()
            }
        }
}
