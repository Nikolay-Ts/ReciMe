package com.sonnenstahl.recime.utils

import android.content.Context
import kotlinx.serialization.Serializable

interface FileManager<T : @Serializable Any> {
    val FILENAME: String

    fun loadData(context: Context): List<T>

    fun saveData(
        context: Context,
        ingredients: List<T>,
    )

    fun deleteData(context: Context): () -> Unit
}
