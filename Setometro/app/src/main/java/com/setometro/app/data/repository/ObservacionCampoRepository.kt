package com.setometro.app.data.repository

import com.setometro.app.data.dao.ObservacionCampoDao
import com.setometro.app.data.entity.ObservacionCampo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObservacionCampoRepository @Inject constructor(
    private val observacionCampoDao: ObservacionCampoDao
) {
    suspend fun getForCoto(cotoId: Int): List<ObservacionCampo> =
        observacionCampoDao.getForCoto(cotoId)

    suspend fun insert(observacion: ObservacionCampo) =
        observacionCampoDao.insert(observacion)
}
