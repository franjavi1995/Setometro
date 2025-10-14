package com.setometro.data

import androidx.room.*

@Dao
interface CotoDao {
    @Query("SELECT * FROM cotos")
    suspend fun getAll(): List<Coto>

    @Query("SELECT * FROM cotos WHERE seleccionado = 1")
    suspend fun getSeleccionados(): List<Coto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg cotos: Coto)

    @Update
    suspend fun update(coto: Coto)
}

@Dao
interface RegistroDiarioDao {
    @Query("SELECT * FROM registros_diarios WHERE coto_id = :cotoId ORDER BY fecha DESC LIMIT 10")
    suspend fun getUltimos10(cotoId: Int): List<RegistroDiario>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg registros: RegistroDiario)
}

@Database(entities = [Coto::class, RegistroDiario::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cotoDao(): CotoDao
    abstract fun registroDiarioDao(): RegistroDiarioDao
}
