package com.setometro.app.data.dao

import androidx.room.*
import com.setometro.app.data.entity.EspecieSeta
import kotlinx.coroutines.flow.Flow

@Dao
interface EspecieSetaDao {
    @Query("SELECT * FROM especies_seta ORDER BY nombre")
    fun getAllEspecies(): Flow<List<EspecieSeta>>
    
    @Query("SELECT * FROM especies_seta WHERE activa = 1 ORDER BY nombre")
    fun getEspeciesActivas(): Flow<List<EspecieSeta>>
    
    @Query("SELECT * FROM especies_seta WHERE id = :id")
    suspend fun getEspecieById(id: Int): EspecieSeta?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEspecie(especie: EspecieSeta)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEspecies(especies: List<EspecieSeta>)
    
    @Update
    suspend fun updateEspecie(especie: EspecieSeta)
    
    @Delete
    suspend fun deleteEspecie(especie: EspecieSeta)
    
    @Query("UPDATE especies_seta SET activa = :activa WHERE id = :id")
    suspend fun updateActiva(id: Int, activa: Boolean)
    
    @Query("SELECT COUNT(*) FROM especies_seta")
    suspend fun getEspecieCount(): Int
    
    @Query("SELECT * FROM especies_seta WHERE activa = 1 ORDER BY nombre")
    suspend fun getEspeciesActivasSync(): List<com.setometro.app.data.entity.EspecieSeta>
}
