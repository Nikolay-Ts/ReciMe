package com.sonnenstahl.recime.utils

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

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

            val meals = MealFileManager.loadData(context).toMutableList()
            meals.add(meal)
            MealFileManager.saveData(context, meals)

            Result.success()
        } catch (e: Exception) {
            Log.e(WORKER_ID, e.toString())
            Result.failure()
        }
}
