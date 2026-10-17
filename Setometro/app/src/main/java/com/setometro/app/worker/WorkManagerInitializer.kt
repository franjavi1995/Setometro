package com.setometro.app.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkManagerInitializer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    fun initializeWeatherUpdates() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
            
        val weatherUpdateRequest = PeriodicWorkRequestBuilder<WeatherUpdateWorker>(
            6, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()
            
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "weather_update_work",
            ExistingPeriodicWorkPolicy.KEEP,
            weatherUpdateRequest
        )
    }
}

