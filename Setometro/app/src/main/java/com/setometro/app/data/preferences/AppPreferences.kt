package com.setometro.app.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("setometro_prefs", Context.MODE_PRIVATE)
    
    // StateFlow para modo noche
    private val _isNightMode = MutableStateFlow(prefs.getBoolean("night_mode", false))
    val isNightMode: StateFlow<Boolean> = _isNightMode.asStateFlow()
    
    fun setNightMode(value: Boolean) {
        prefs.edit { putBoolean("night_mode", value) }
        _isNightMode.value = value
    }

    // StateFlow para unidades de temperatura
    private val _isCelsius = MutableStateFlow(prefs.getBoolean("is_celsius", true))
    val isCelsius: StateFlow<Boolean> = _isCelsius.asStateFlow()
    
    fun setCelsius(value: Boolean) {
        prefs.edit { putBoolean("is_celsius", value) }
        _isCelsius.value = value
    }

    // Información de la app
    fun getAppVersion(): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (e: Exception) {
            "1.0.0"
        }
    }

    fun getDatabaseSize(): String {
        return try {
            val dbPath = context.getDatabasePath("setometro_database").absolutePath
            val file = java.io.File(dbPath)
            if (file.exists()) {
                val sizeInMB = file.length() / (1024 * 1024)
                "${sizeInMB} MB"
            } else {
                "0 MB"
            }
        } catch (e: Exception) {
            "N/A"
        }
    }

    fun clearCache() {
        try {
            // Limpiar caché de la aplicación
            val cacheDir = context.cacheDir
            if (cacheDir.exists()) {
                cacheDir.deleteRecursively()
            }
        } catch (e: Exception) {
            // Error silencioso
        }
    }
}
