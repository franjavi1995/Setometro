package com.setometro.app.data.dao

import androidx.room.*
import com.setometro.app.data.entity.RegistroDiario
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroDiarioDao {
    @Query("SELECT * FROM registros_diarios WHERE cotoId = :cotoId ORDER BY fecha DESC LIMIT 10")
    fun getUltimos10Dias(cotoId: Int): Flow<List<RegistroDiario>>
    
    @Query("SELECT * FROM registros_diarios WHERE cotoId = :cotoId AND fecha = :fecha")
    suspend fun getRegistroPorFecha(cotoId: Int, fecha: String): RegistroDiario?
    
    @Query("SELECT * FROM registros_diarios WHERE cotoId = :cotoId ORDER BY fecha DESC")
    fun getRegistrosPorCoto(cotoId: Int): Flow<List<RegistroDiario>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistro(registro: RegistroDiario)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistros(registros: List<RegistroDiario>)
    
    @Update
    suspend fun updateRegistro(registro: RegistroDiario)
    
    @Delete
    suspend fun deleteRegistro(registro: RegistroDiario)
    
    @Query("SELECT AVG(lluviaMm) FROM registros_diarios WHERE cotoId = :cotoId AND fecha >= :fechaInicio")
    suspend fun getLluviaPromedio(cotoId: Int, fechaInicio: String): Double?
    
    @Query("SELECT AVG(temperaturaMedia) FROM registros_diarios WHERE cotoId = :cotoId AND fecha >= :fechaInicio")
    suspend fun getTemperaturaPromedio(cotoId: Int, fechaInicio: String): Double?
    
    @Query("SELECT * FROM registros_diarios WHERE cotoId = :cotoId ORDER BY fecha DESC")
    fun getRegistrosByCotoId(cotoId: Int): Flow<List<RegistroDiario>>
    
    @Query("SELECT * FROM registros_diarios WHERE cotoId = :cotoId ORDER BY fecha DESC")
    suspend fun getRegistrosByCotoIdSync(cotoId: Int): List<RegistroDiario>
    
    @Query("SELECT * FROM registros_diarios WHERE cotoId = :cotoId ORDER BY fecha DESC LIMIT 15")
    suspend fun getLast15DaysWeather(cotoId: Int): List<RegistroDiario>
    
    @Query("SELECT SUM(lluviaMm) FROM registros_diarios WHERE cotoId = :cotoId AND fecha >= date('now', '-15 days')")
    suspend fun getRainfallSum(cotoId: Int): Double
    
    @Query("SELECT AVG(temperaturaMedia) FROM registros_diarios WHERE cotoId = :cotoId AND fecha >= date('now', '-15 days')")
    suspend fun getAverageTemperature(cotoId: Int): Double
}
