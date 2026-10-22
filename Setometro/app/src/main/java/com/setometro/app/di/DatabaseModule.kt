package com.setometro.app.di

import android.content.Context
import com.setometro.app.data.dao.CotoDao
import com.setometro.app.data.dao.RegistroDiarioDao
import com.setometro.app.data.dao.EspecieSetaDao
import com.setometro.app.data.dao.ObservacionCampoDao
import com.setometro.app.data.database.SetometroDatabase
import com.setometro.app.data.preferences.AppPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SetometroDatabase {
        return SetometroDatabase.getDatabase(context)
    }
    
    @Provides
    fun provideCotoDao(database: SetometroDatabase): CotoDao {
        return database.cotoDao()
    }
    
    @Provides
    fun provideRegistroDiarioDao(database: SetometroDatabase): RegistroDiarioDao {
        return database.registroDiarioDao()
    }
    
    @Provides
    fun provideEspecieSetaDao(database: SetometroDatabase): EspecieSetaDao {
        return database.especieSetaDao()
    }

    @Provides
    fun provideObservacionCampoDao(database: SetometroDatabase): ObservacionCampoDao {
        return database.observacionCampoDao()
    }
    
    @Provides
    @Singleton
    fun provideCotoRepository(
        cotoDao: CotoDao
    ): com.setometro.app.data.repository.CotoRepository {
        return com.setometro.app.data.repository.CotoRepository(cotoDao)
    }
    
    @Provides
    @Singleton
    fun provideRegistroRepository(
        registroDiarioDao: RegistroDiarioDao
    ): com.setometro.app.data.repository.RegistroRepository {
        return com.setometro.app.data.repository.RegistroRepository(registroDiarioDao)
    }
    
    @Provides
    @Singleton
    fun provideEspecieSetaRepository(
        especieSetaDao: EspecieSetaDao
    ): com.setometro.app.data.repository.EspecieSetaRepository {
        return com.setometro.app.data.repository.EspecieSetaRepository(especieSetaDao)
    }

    @Provides
    @Singleton
    fun provideObservacionCampoRepository(
        observacionCampoDao: ObservacionCampoDao
    ): com.setometro.app.data.repository.ObservacionCampoRepository {
        return com.setometro.app.data.repository.ObservacionCampoRepository(observacionCampoDao)
    }
    
    @Provides
    @Singleton
    fun provideAppPreferences(
        @ApplicationContext context: Context
    ): AppPreferences {
        return AppPreferences(context)
    }
}
