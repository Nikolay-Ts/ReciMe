package com.sonnenstahl.recime.utils

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/**
 * class that schedules the task
 */
class DailyMealTask(
    private val context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {
    companion object {
        const val WORKER_ID: String = "DailyMealTask"
    }

    override suspend fun doWork(): Result =
        try {
            val meal = Client.getRandomMeal()
            if (meal == null) {
                throw Exception("Could not get a meal from the API")
            }

            MealFileManager.saveData(context, listOf(meal))

            Result.success()
        } catch (e: Exception) {
            Log.e(WORKER_ID, e.toString())
            Result.failure()
        }
}
