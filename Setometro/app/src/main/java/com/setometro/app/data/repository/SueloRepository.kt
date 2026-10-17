package com.setometro.app.data.repository

import com.setometro.app.data.dao.DatosSueloDao
import com.setometro.app.data.entity.DatosSuelo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SueloRepository @Inject constructor(
    private val datosSueloDao: DatosSueloDao
) {
    
    suspend fun getDatosSuelo(cotoId: Int): DatosSuelo? {
        return datosSueloDao.getDatosSueloByCotoId(cotoId)
    }
    
    suspend fun saveDatosSuelo(datosSuelo: DatosSuelo) {
        datosSueloDao.insertDatosSuelo(datosSuelo)
    }
    
    suspend fun updateDatosSuelo(datosSuelo: DatosSuelo) {
        datosSueloDao.updateDatosSuelo(datosSuelo)
    }
    
    suspend fun deleteDatosSuelo(cotoId: Int) {
        datosSueloDao.deleteDatosSueloByCotoId(cotoId)
    }
    
    /**
     * Estima datos del suelo basándose en coordenadas geográficas
     * Esta es una estimación básica basada en patrones geográficos generales
     */
    suspend fun estimateDatosSueloFromCoordinates(
        cotoId: Int,
        latitude: Double,
        longitude: Double,
        altitud: Double
    ): DatosSuelo {
        
        // Estimación básica basada en coordenadas geográficas
        val tipoSuelo = estimateTipoSuelo(latitude, longitude, altitud)
        val phSuelo = estimatePhSuelo(latitude, longitude)
        val drenaje = estimateDrenaje(altitud, tipoSuelo)
        val materiaOrganica = estimateMateriaOrganica(tipoSuelo, altitud)
        val textura = estimateTextura(tipoSuelo)
        
        return DatosSuelo(
            cotoId = cotoId,
            tipoSuelo = tipoSuelo,
            phSuelo = phSuelo,
            drenaje = drenaje,
            materiaOrganica = materiaOrganica,
            textura = textura
        )
    }
    
    private fun estimateTipoSuelo(latitude: Double, longitude: Double, altitud: Double): String {
        return when {
            // Zonas montañosas (altitud > 1000m)
            altitud > 1000 -> "Orgánico"
            // Zonas costeras (latitud baja y longitud cerca del mar)
            latitude < 40 && (longitude > -10 || longitude < -5) -> "Arenoso"
            // Zonas del interior
            else -> "Mixto"
        }
    }
    
    private fun estimatePhSuelo(latitude: Double, longitude: Double): Double {
        return when {
            // Zonas graníticas (más ácidas)
            latitude > 42 && longitude > -8 -> 5.5
            // Zonas calcáreas (más alcalinas)
            latitude < 40 && longitude > -3 -> 7.2
            // Zonas intermedias
            else -> 6.5
        }
    }
    
    private fun estimateDrenaje(altitud: Double, tipoSuelo: String): String {
        return when {
            altitud > 800 && tipoSuelo == "Arenoso" -> "Bueno"
            altitud < 300 && tipoSuelo == "Arcilloso" -> "Malo"
            else -> "Regular"
        }
    }
    
    private fun estimateMateriaOrganica(tipoSuelo: String, altitud: Double): Double {
        return when {
            tipoSuelo == "Orgánico" -> 8.0
            tipoSuelo == "Mixto" && altitud > 500 -> 5.0
            tipoSuelo == "Arenoso" -> 2.0
            else -> 3.5
        }
    }
    
    private fun estimateTextura(tipoSuelo: String): String {
        return when (tipoSuelo) {
            "Arenoso" -> "Gruesa"
            "Arcilloso" -> "Fina"
            "Orgánico" -> "Media"
            else -> "Media"
        }
    }
}



