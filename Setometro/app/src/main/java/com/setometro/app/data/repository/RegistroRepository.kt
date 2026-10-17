package com.setometro.app.data.repository

import com.setometro.app.data.dao.RegistroDiarioDao
import com.setometro.app.data.entity.RegistroDiario
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistroRepository @Inject constructor(
    private val registroDiarioDao: RegistroDiarioDao
) {
    
    fun getUltimos10Dias(cotoId: Int): Flow<List<RegistroDiario>> {
        return registroDiarioDao.getUltimos10Dias(cotoId)
    }
    
    suspend fun getRegistroPorFecha(cotoId: Int, fecha: String): RegistroDiario? {
        return registroDiarioDao.getRegistroPorFecha(cotoId, fecha)
    }
    
    fun getRegistrosPorCoto(cotoId: Int): Flow<List<RegistroDiario>> {
        return registroDiarioDao.getRegistrosPorCoto(cotoId)
    }
    
    suspend fun insertRegistro(registro: RegistroDiario) {
        registroDiarioDao.insertRegistro(registro)
    }
    
    suspend fun insertRegistros(registros: List<RegistroDiario>) {
        registroDiarioDao.insertRegistros(registros)
    }
    
    suspend fun updateRegistro(registro: RegistroDiario) {
        registroDiarioDao.updateRegistro(registro)
    }
    
    suspend fun deleteRegistro(registro: RegistroDiario) {
        registroDiarioDao.deleteRegistro(registro)
    }
    
    suspend fun getLluviaPromedio(cotoId: Int, fechaInicio: String): Double? {
        return registroDiarioDao.getLluviaPromedio(cotoId, fechaInicio)
    }
    
    suspend fun getTemperaturaPromedio(cotoId: Int, fechaInicio: String): Double? {
        return registroDiarioDao.getTemperaturaPromedio(cotoId, fechaInicio)
    }
    
    fun getRegistrosByCotoId(cotoId: Int): Flow<List<RegistroDiario>> {
        return registroDiarioDao.getRegistrosByCotoId(cotoId)
    }
    
    suspend fun getRegistrosForCotoSync(cotoId: Int): List<RegistroDiario> {
        return registroDiarioDao.getRegistrosByCotoIdSync(cotoId)
    }
}





