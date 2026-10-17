package com.setometro.app.data.repository

import com.setometro.app.data.dao.CotoDao
import com.setometro.app.data.entity.Coto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CotoRepository @Inject constructor(
    private val cotoDao: CotoDao
) {
    fun getCotosSeleccionados(): Flow<List<Coto>> = cotoDao.getCotosSeleccionados()
    
    fun getAllCotos(): Flow<List<Coto>> = cotoDao.getAllCotos()
    
    suspend fun getCotoById(id: Int): Coto? = cotoDao.getCotoById(id)
    
    suspend fun insertCoto(coto: Coto): Long = cotoDao.insertCoto(coto)
    
    suspend fun insertCotos(cotos: List<Coto>) = cotoDao.insertCotos(cotos)
    
    suspend fun updateCoto(coto: Coto) = cotoDao.updateCoto(coto)
    
    suspend fun deleteCoto(coto: Coto) = cotoDao.deleteCoto(coto)
    
    suspend fun updateSeleccion(id: Int, seleccionado: Boolean) = 
        cotoDao.updateSeleccion(id, seleccionado)
    
    suspend fun getCotoCount(): Int = cotoDao.getCotoCount()
    
    suspend fun getCotosSeleccionadosSync(): List<Coto> = cotoDao.getCotosSeleccionadosSync()
}
