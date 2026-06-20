package com.cocode.tmsmeasurement.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [MeasurementEntity::class, TreatmentEntity::class],
    version = 1,
    exportSchema = true
)
abstract class TmsDatabase : RoomDatabase() {
    abstract fun measurementDao(): MeasurementDao
    abstract fun treatmentDao(): TreatmentDao

    companion object {
        private const val DB_NAME = "tms.db"

        @Volatile
        private var instance: TmsDatabase? = null

        fun getInstance(context: Context): TmsDatabase {
            return instance ?: synchronized(this) {
                instance ?: build(context).also { instance = it }
            }
        }

        private fun build(context: Context): TmsDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                TmsDatabase::class.java,
                DB_NAME
            ).build()
        }
    }
}
