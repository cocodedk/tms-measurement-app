package com.cocode.tmsmeasurement.data

import com.cocode.tmsmeasurement.data.local.TreatmentEntity
import kotlinx.coroutines.flow.Flow

/**
 * Read/write port over the treatment sessions attached to a measurement.
 *
 * Implemented in production by [TmsRepository] (backed by Room). Kept as a
 * separate interface so the ViewModel layer can be unit-tested on the plain JVM
 * with a fake, without an Android emulator or Robolectric.
 *
 * All mutations are suspending (Room access on a background dispatcher — never
 * the main thread); reads are exposed as a [Flow] that emits on every change.
 * Deleting the parent measurement cascades to its treatments at the database
 * level (foreign key ON DELETE CASCADE), so there is no explicit bulk delete.
 */
interface TreatmentRepository {
    fun observeTreatments(measurementId: String): Flow<List<TreatmentEntity>>
    suspend fun saveTreatment(entity: TreatmentEntity)
    suspend fun deleteTreatment(entity: TreatmentEntity)
    suspend fun getTreatment(id: Long): TreatmentEntity?
}
