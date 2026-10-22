package com.setometro.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.setometro.app.data.entity.ObservacionCampo

@Dao
interface ObservacionCampoDao {
    @Query(
        "SELECT * FROM observaciones_campo WHERE cotoId = :cotoId " +
            "ORDER BY fechaObservacion DESC"
    )
    suspend fun getForCoto(cotoId: Int): List<ObservacionCampo>

    @Insert
    suspend fun insert(observacion: ObservacionCampo)
}
