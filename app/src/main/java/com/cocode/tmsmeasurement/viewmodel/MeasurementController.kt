package com.cocode.tmsmeasurement.viewmodel

import com.cocode.tmsmeasurement.data.LegacyImporter
import com.cocode.tmsmeasurement.data.MeasurementRepository
import com.cocode.tmsmeasurement.data.TreatmentRepository
import com.cocode.tmsmeasurement.data.local.MeasurementEntity
import com.cocode.tmsmeasurement.data.local.TreatmentEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Plain (non-Android) core of the measurement screen's state and actions.
 *
 * Extracted from [MeasurementViewModel] so the real logic — one-time legacy
 * import on init, the observed list, and add/delete — can be unit-tested on the
 * JVM with a fake [MeasurementRepository], a [TestScope], and a test dispatcher.
 *
 * [scope] is the ViewModel's coroutine scope; [ioDispatcher] keeps all Room
 * writes off the main thread.
 */
class MeasurementController(
    private val repository: MeasurementRepository,
    private val importer: LegacyImporter,
    private val scope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher,
    private val treatments: TreatmentRepository
) {
    val measurements: StateFlow<List<MeasurementEntity>> =
        repository.observeMeasurements().stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = emptyList()
        )

    // One hot StateFlow per measurement id so the detail screen re-collects the
    // same instance across recompositions instead of restarting the query.
    private val treatmentFlows = mutableMapOf<String, StateFlow<List<TreatmentEntity>>>()

    init {
        scope.launch(ioDispatcher) {
            importer.importIfNeeded()
        }
    }

    fun addMeasurement(entity: MeasurementEntity) {
        scope.launch(ioDispatcher) {
            repository.addMeasurement(entity)
        }
    }

    fun deleteMeasurement(id: String) {
        scope.launch(ioDispatcher) {
            repository.deleteMeasurement(id)
        }
    }

    fun treatmentsFor(measurementId: String): StateFlow<List<TreatmentEntity>> =
        treatmentFlows.getOrPut(measurementId) {
            treatments.observeTreatments(measurementId).stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
                initialValue = emptyList()
            )
        }

    fun saveTreatment(entity: TreatmentEntity) {
        scope.launch(ioDispatcher) {
            treatments.saveTreatment(entity)
        }
    }

    fun deleteTreatment(entity: TreatmentEntity) {
        scope.launch(ioDispatcher) {
            treatments.deleteTreatment(entity)
        }
    }

    suspend fun getTreatment(id: Long): TreatmentEntity? =
        withContext(ioDispatcher) { treatments.getTreatment(id) }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
