package com.sonnenstahl.recime

import android.app.Application
import com.sonnenstahl.recime.utils.DailyMealScheduler

/**
 * this is created so that the scheduler can be called even on device reboot if the app
 * has not been opened
 */
class ReciMe : Application() {
    override fun onCreate() {
        super.onCreate()
        DailyMealScheduler.schedule(this)
    }
}
