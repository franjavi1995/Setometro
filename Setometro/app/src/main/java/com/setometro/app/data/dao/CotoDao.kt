package com.setometro.app.data.dao

import androidx.room.*
import com.setometro.app.data.entity.Coto
import kotlinx.coroutines.flow.Flow

@Dao
interface CotoDao {
    @Query("SELECT * FROM coto ORDER BY nombre ASC")
    fun getAllCotos(): Flow<List<Coto>>

    @Query("SELECT * FROM coto WHERE seleccionado = 1 ORDER BY nombre ASC")
    fun getCotosSeleccionados(): Flow<List<Coto>>

    @Query("SELECT * FROM coto WHERE id = :id")
    suspend fun getCotoById(id: Int): Coto?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoto(coto: Coto): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCotos(cotos: List<Coto>)

    @Update
    suspend fun updateCoto(coto: Coto)

    @Delete
    suspend fun deleteCoto(coto: Coto)

    @Query("DELETE FROM coto WHERE id = :id")
    suspend fun deleteCotoById(id: Int)

    @Query("DELETE FROM coto")
    suspend fun deleteAllCotos()

    @Query("UPDATE coto SET seleccionado = :seleccionado WHERE id = :id")
    suspend fun updateSeleccion(id: Int, seleccionado: Boolean)

    @Query("SELECT COUNT(*) FROM coto")
    suspend fun getCotoCount(): Int

    @Query("SELECT * FROM coto WHERE seleccionado = 1 ORDER BY nombre ASC")
    suspend fun getCotosSeleccionadosSync(): List<Coto>
}
