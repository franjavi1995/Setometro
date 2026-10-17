package com.setometro.app.data.dao

import androidx.room.*
import com.setometro.app.data.entity.DatosSuelo
import kotlinx.coroutines.flow.Flow

@Dao
interface DatosSueloDao {
    
    @Query("SELECT * FROM datos_suelo WHERE cotoId = :cotoId")
    suspend fun getDatosSueloByCotoId(cotoId: Int): DatosSuelo?
    
    @Query("SELECT * FROM datos_suelo WHERE cotoId = :cotoId")
    fun getDatosSueloByCotoIdFlow(cotoId: Int): Flow<DatosSuelo?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDatosSuelo(datosSuelo: DatosSuelo)
    
    @Update
    suspend fun updateDatosSuelo(datosSuelo: DatosSuelo)
    
    @Delete
    suspend fun deleteDatosSuelo(datosSuelo: DatosSuelo)
    
    @Query("DELETE FROM datos_suelo WHERE cotoId = :cotoId")
    suspend fun deleteDatosSueloByCotoId(cotoId: Int)
}



