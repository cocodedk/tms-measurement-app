package com.cocode.tmsmeasurement.data

import com.cocode.tmsmeasurement.data.local.MeasurementEntity
import kotlinx.coroutines.flow.Flow

/**
 * Read/write port over the persisted measurements.
 *
 * Implemented in production by [TmsRepository] (backed by Room). Kept as an
 * interface so the ViewModel layer can be unit-tested on the plain JVM with a
 * fake, without an Android emulator or Robolectric.
 *
 * All mutations are suspending (Room access on a background dispatcher — never
 * the main thread); reads are exposed as a [Flow] that emits on every change.
 */
interface MeasurementRepository {
    fun observeMeasurements(): Flow<List<MeasurementEntity>>
    suspend fun addMeasurement(entity: MeasurementEntity)
    suspend fun deleteMeasurement(id: String)
}
