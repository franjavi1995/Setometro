package com.setometro.app.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.setometro.app.data.dao.CotoDao
import com.setometro.app.data.dao.RegistroDiarioDao
import com.setometro.app.data.dao.EspecieSetaDao
import com.setometro.app.data.entity.Coto
import com.setometro.app.data.entity.RegistroDiario
import com.setometro.app.data.entity.EspecieSeta
import com.setometro.app.data.dao.ObservacionCampoDao
import com.setometro.app.data.entity.ObservacionCampo

@Database(
    entities = [Coto::class, RegistroDiario::class, EspecieSeta::class, ObservacionCampo::class],
    version = 5,
    exportSchema = false
)
abstract class SetometroDatabase : RoomDatabase() {
    abstract fun cotoDao(): CotoDao
    abstract fun registroDiarioDao(): RegistroDiarioDao
    abstract fun especieSetaDao(): EspecieSetaDao
    abstract fun observacionCampoDao(): ObservacionCampoDao
    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DELETE FROM probabilidades_seta")
                db.execSQL("DELETE FROM registros_diarios")
                db.execSQL("DELETE FROM especies_seta")
                db.execSQL(
                    """
                    DELETE FROM coto
                    WHERE provincia IN ('Zamora', 'Badajoz')
                    AND nombre IN (
                        'Bosque de Robleda de Sanabria',
                        'Monte de Puebla de Sanabria',
                        'Lago de Sanabria - Zona Norte',
                        'Sierra de la Culebra - Sanabria',
                        'Monte de Trefacio',
                        'Valle de San Martín de Castañeda',
                        'Monte de Lubián',
                        'Bosque de Requejo',
                        'Sierra de Tentudía - Monasterio',
                        'Monte de Zafra - Dehesa',
                        'Sierra de Tentudía - Zona Norte',
                        'Valle de Zafra - Arroyo',
                        'Monte de Tentudía - Ladera Sur',
                        'Dehesa de Zafra - Monte Alto',
                        'Sierra de Tentudía - Zona Este',
                        'Valle de Tentudía - Arroyo Hondo',
                        'Monte de Zafra - Zona Oeste',
                        'Zona de Zafra - Monte Público'
                    )
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE IF EXISTS probabilidades_seta")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE coto ADD COLUMN fuenteCoordenadas TEXT NOT NULL DEFAULT 'MANUAL'"
                )
                db.execSQL(
                    "ALTER TABLE coto ADD COLUMN precisionCoordenadasMetros REAL"
                )
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                        CREATE TABLE IF NOT EXISTS observaciones_campo (
                            id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                            cotoId INTEGER,
                            nombreUbicacion TEXT NOT NULL,
                            latitud REAL NOT NULL,
                            longitud REAL NOT NULL,
                            fuenteCoordenadas TEXT NOT NULL,
                            precisionCoordenadasMetros REAL,
                            especieNombre TEXT NOT NULL,
                            nombreCientifico TEXT,
                            resultado TEXT NOT NULL,
                            fechaObservacion INTEGER NOT NULL,
                            ejemplares INTEGER,
                            nota TEXT,
                            FOREIGN KEY(cotoId) REFERENCES coto(id)
                                ON UPDATE NO ACTION ON DELETE SET NULL
                        )
                    """.trimIndent()
                )
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_observaciones_campo_cotoId " +
                        "ON observaciones_campo(cotoId)"
                )
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_observaciones_campo_fechaObservacion " +
                        "ON observaciones_campo(fechaObservacion)"
                )
            }
        }

        @Volatile
        private var INSTANCE: SetometroDatabase? = null
        
        fun getDatabase(context: Context): SetometroDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SetometroDatabase::class.java,
                    "setometro_database"
                )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
