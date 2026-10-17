package com.setometro.app

import android.app.Application
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SetometroApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        WorkManager.getInstance(this).cancelUniqueWork("weather_update_work")
    }
}
