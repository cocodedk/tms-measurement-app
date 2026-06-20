package com.cocode.tmsmeasurement.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TreatmentDao {
    @Query("SELECT * FROM treatments WHERE measurementId = :mid ORDER BY dateMs DESC")
    fun observeForMeasurement(mid: String): Flow<List<TreatmentEntity>>

    @Upsert
    suspend fun upsert(treatment: TreatmentEntity)

    @Delete
    suspend fun delete(treatment: TreatmentEntity)

    @Query("SELECT * FROM treatments WHERE id = :id")
    suspend fun getById(id: Long): TreatmentEntity?
}
