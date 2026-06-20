package com.cocode.tmsmeasurement.data

import com.cocode.tmsmeasurement.data.local.MeasurementDao
import com.cocode.tmsmeasurement.data.local.MeasurementEntity
import com.cocode.tmsmeasurement.data.local.TreatmentDao
import com.cocode.tmsmeasurement.data.local.TreatmentEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room-backed implementation of [MeasurementRepository] and [TreatmentRepository].
 *
 * Also satisfies [MeasurementWriter] (via [MeasurementDao.insertAllIfAbsent]) so the
 * one-time [LegacyImporter] writes through the same store as the live app. Treatment
 * reads/writes go through [TreatmentDao]; deleting a measurement cascades to its
 * treatments at the SQLite level (foreign key ON DELETE CASCADE on
 * [com.cocode.tmsmeasurement.data.local.TreatmentEntity]). All access is suspending —
 * no main-thread queries.
 */
class TmsRepository(
    private val measurementDao: MeasurementDao,
    private val treatmentDao: TreatmentDao
) : MeasurementRepository, MeasurementWriter, TreatmentRepository {

    override fun observeMeasurements(): Flow<List<MeasurementEntity>> =
        measurementDao.observeAll()

    override suspend fun addMeasurement(entity: MeasurementEntity) {
        measurementDao.upsert(entity)
    }

    override suspend fun deleteMeasurement(id: String) {
        measurementDao.deleteById(id)
    }

    override suspend fun insertAllIfAbsent(items: List<MeasurementEntity>) {
        measurementDao.insertAllIfAbsent(items)
    }

    override suspend fun count(): Int = measurementDao.count()

    override fun observeTreatments(measurementId: String): Flow<List<TreatmentEntity>> =
        treatmentDao.observeForMeasurement(measurementId)

    override suspend fun saveTreatment(entity: TreatmentEntity) {
        treatmentDao.upsert(entity)
    }

    override suspend fun deleteTreatment(entity: TreatmentEntity) {
        treatmentDao.delete(entity)
    }

    override suspend fun getTreatment(id: Long): TreatmentEntity? =
        treatmentDao.getById(id)
}
