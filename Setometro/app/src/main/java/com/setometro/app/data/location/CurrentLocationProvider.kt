package com.setometro.app.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.CancellationSignal
import android.os.Looper
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class CurrentLocationProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location {
        val hasFinePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        val hasCoarsePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        if (!hasFinePermission && !hasCoarsePermission) {
            throw SecurityException("Concede permiso de ubicación para consultar este punto.")
        }

        val enabledProviders = locationManager.getProviders(true)
        val preferredProviders = if (hasFinePermission) {
            listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
        } else {
            listOf(LocationManager.NETWORK_PROVIDER)
        }
        val provider = preferredProviders.firstOrNull { it in enabledProviders }
            ?: throw IllegalStateException(
                "No hay un proveedor de ubicación habilitado. Activa la ubicación del dispositivo."
            )

        return withTimeoutOrNull(30_000) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requestCurrentLocation(provider)
            } else {
                requestSingleLocation(provider)
            }
        } ?: throw IllegalStateException(
            "No se pudo obtener una ubicación en 30 segundos. Comprueba la señal y vuelve a intentarlo."
        )
    }

    @SuppressLint("MissingPermission")
    private suspend fun requestCurrentLocation(provider: String): Location? =
        suspendCancellableCoroutine { continuation ->
            val cancellationSignal = CancellationSignal()
            continuation.invokeOnCancellation { cancellationSignal.cancel() }

            locationManager.getCurrentLocation(
                provider,
                cancellationSignal,
                ContextCompat.getMainExecutor(context)
            ) { location ->
                if (!continuation.isActive) return@getCurrentLocation
                if (location == null) {
                    continuation.resumeWithException(
                        IllegalStateException(
                            "El dispositivo no devolvió una ubicación. Comprueba la señal y vuelve a intentarlo."
                        )
                    )
                } else {
                    continuation.resume(location)
                }
            }
        }

    @Suppress("DEPRECATION", "MissingPermission")
    private suspend fun requestSingleLocation(provider: String): Location? =
        suspendCancellableCoroutine { continuation ->
            val listener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    if (continuation.isActive) continuation.resume(location)
                    locationManager.removeUpdates(this)
                }

                override fun onProviderEnabled(provider: String) = Unit

                override fun onProviderDisabled(provider: String) = Unit

                @Deprecated("Deprecated in Android API")
                override fun onStatusChanged(provider: String?, status: Int, extras: android.os.Bundle?) = Unit
            }

            continuation.invokeOnCancellation {
                locationManager.removeUpdates(listener)
            }
            try {
                locationManager.requestSingleUpdate(provider, listener, Looper.getMainLooper())
            } catch (exception: Exception) {
                if (continuation.isActive) continuation.resumeWithException(exception)
            }
        }
}
