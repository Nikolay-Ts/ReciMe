package com.sonnenstahl.recime.utils

import android.content.Context
import kotlinx.serialization.Serializable

/**
 * interface for the file managers to load and save a serializable item to/from disk
 *
 * @param FILENAME to store the data at
 */
interface FileManager<T : @Serializable Any> {
    val FILENAME: String

    /**
     * load the data from the [FILENAME]
     *
     * @param context
     *
     * @return a list of the serializable objects or empty list
     */
    fun loadData(context: Context): List<T>

    /**
     * save the data from the [FILENAME]
     *
     * @param context
     * @param ingredients to be saved
     *
     */
    fun saveData(
        context: Context,
        ingredients: List<T>,
    )

    /**
     * delete the data by deleting the file
     *
     * @param context
     */
    fun deleteData(context: Context): () -> Unit
}
