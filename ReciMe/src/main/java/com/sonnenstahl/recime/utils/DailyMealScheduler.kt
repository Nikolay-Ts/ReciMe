package com.sonnenstahl.recime.utils

import android.content.Context
import androidx.work.*
import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

/**
 * object called at the beginning of the app to run in the background
 */
object DailyMealScheduler {
    /**
     * a function that loads a new random daily meal at midnight UTC
     *
     * @param context of the app
     */
    fun schedule(context: Context) {
        val now = Instant.now().atZone(ZoneOffset.UTC)
        val nextMidnight =
            now
                .plusDays(1)
                .toLocalDate()
                .atStartOfDay()
                .atZone(ZoneOffset.UTC)
        val initialDelay = Duration.between(now, nextMidnight)

        val workRequest =
            PeriodicWorkRequestBuilder<DailyMealTask>(
                24,
                TimeUnit.HOURS,
            ).setInitialDelay(initialDelay.toMillis(), TimeUnit.MILLISECONDS)
                .addTag(DailyMealTask.WORKER_ID)
                .setConstraints(
                    Constraints
                        .Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build(),
                ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            DailyMealTask.WORKER_ID,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest,
        )
    }
}
