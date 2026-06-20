package com.cocode.tmsmeasurement.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasurementDao {
    @Query("SELECT * FROM measurements ORDER BY timestampMs DESC")
    fun observeAll(): Flow<List<MeasurementEntity>>

    @Upsert
    suspend fun upsert(measurement: MeasurementEntity)

    @Query("DELETE FROM measurements WHERE id = :id")
    suspend fun deleteById(id: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllIfAbsent(measurements: List<MeasurementEntity>)

    @Query("SELECT COUNT(*) FROM measurements")
    suspend fun count(): Int
}
