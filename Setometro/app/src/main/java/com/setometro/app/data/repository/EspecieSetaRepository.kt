package com.setometro.app.data.repository

import com.setometro.app.data.dao.EspecieSetaDao
import com.setometro.app.data.entity.EspecieSeta
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EspecieSetaRepository @Inject constructor(
    private val especieSetaDao: EspecieSetaDao
) {
    
    fun getAllEspecies(): Flow<List<EspecieSeta>> {
        return especieSetaDao.getAllEspecies()
    }
    
    fun getEspeciesActivas(): Flow<List<EspecieSeta>> {
        return especieSetaDao.getEspeciesActivas()
    }
    
    suspend fun getEspecieById(id: Int): EspecieSeta? {
        return especieSetaDao.getEspecieById(id)
    }
    
    suspend fun insertEspecie(especie: EspecieSeta) {
        especieSetaDao.insertEspecie(especie)
    }
    
    suspend fun insertEspecies(especies: List<EspecieSeta>) {
        especieSetaDao.insertEspecies(especies)
    }
    
    suspend fun updateEspecie(especie: EspecieSeta) {
        especieSetaDao.updateEspecie(especie)
    }
    
    suspend fun deleteEspecie(especie: EspecieSeta) {
        especieSetaDao.deleteEspecie(especie)
    }
    
    suspend fun updateActiva(id: Int, activa: Boolean) {
        especieSetaDao.updateActiva(id, activa)
    }
    
    suspend fun getEspecieCount(): Int {
        return especieSetaDao.getEspecieCount()
    }
}





