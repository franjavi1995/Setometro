package com.setometro.app.data.repository

import android.os.SystemClock
import com.setometro.app.data.api.PlaceSearchApiService
import com.setometro.app.data.model.PlaceSearchHit
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Singleton
class PlaceSearchRepository @Inject constructor(
    private val apiService: PlaceSearchApiService
) {
    private val requestMutex = Mutex()
    private var lastRequestAt = 0L

    suspend fun search(query: String): List<PlaceSearchHit> {
        val normalizedQuery = query.trim()
        require(normalizedQuery.length >= MIN_QUERY_LENGTH) {
            "Escribe al menos $MIN_QUERY_LENGTH caracteres para buscar."
        }

        return requestMutex.withLock {
            val now = SystemClock.elapsedRealtime()
            val waitMillis = MIN_REQUEST_INTERVAL_MILLIS - (now - lastRequestAt)
            if (lastRequestAt != 0L && waitMillis > 0L) {
                delay(waitMillis)
            }

            lastRequestAt = SystemClock.elapsedRealtime()
            val response = apiService.search(normalizedQuery)
            if (!response.isSuccessful) {
                throw IOException("OpenStreetMap devolvió el error HTTP ${response.code()}.")
            }

            response.body()
                ?.mapNotNull { result ->
                    val latitude = result.lat.toDoubleOrNull()
                    val longitude = result.lon.toDoubleOrNull()
                    if (latitude == null || longitude == null ||
                        latitude !in -90.0..90.0 || longitude !in -180.0..180.0
                    ) {
                        null
                    } else {
                        val name = result.name?.takeIf(String::isNotBlank)
                            ?: result.displayName?.substringBefore(',')
                            ?: return@mapNotNull null
                        val province = result.address?.province
                            ?: result.address?.state
                            ?: result.address?.county
                        PlaceSearchHit(
                            name = name,
                            description = result.displayName ?: name,
                            latitude = latitude,
                            longitude = longitude,
                            province = province
                        )
                    }
                }
                ?: throw IOException("OpenStreetMap respondió sin resultados de búsqueda.")
        }
    }

    private companion object {
        const val MIN_QUERY_LENGTH = 3
        const val MIN_REQUEST_INTERVAL_MILLIS = 1_000L
    }
}
