package com.setometro.app.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.setometro.app.data.repository.CotoRepository
import com.setometro.app.data.repository.WeatherRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class WeatherUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val cotoRepository: CotoRepository,
    private val weatherRepository: WeatherRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val cotos = cotoRepository.getCotosSeleccionadosSync()
            for (coto in cotos) {
                val result = weatherRepository.fetchWeather(coto.latitud, coto.longitud)
                if (result.previousDays.isEmpty()) {
                    return Result.retry()
                }
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

