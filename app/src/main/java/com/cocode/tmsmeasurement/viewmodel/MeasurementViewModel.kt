package com.cocode.tmsmeasurement.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cocode.tmsmeasurement.MeasurementStorage
import com.cocode.tmsmeasurement.data.LegacyImporter
import com.cocode.tmsmeasurement.data.PrefsMigrationFlag
import com.cocode.tmsmeasurement.data.StorageLegacySource
import com.cocode.tmsmeasurement.data.TmsRepository
import com.cocode.tmsmeasurement.data.local.MeasurementEntity
import com.cocode.tmsmeasurement.data.local.TmsDatabase
import com.cocode.tmsmeasurement.data.local.TreatmentEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Measurement screen ViewModel.
 *
 * Builds the Room repository from [TmsDatabase.getInstance], runs the one-time
 * legacy import on init (off the main thread), and exposes the live measurement
 * list plus add/delete. All real logic lives in [MeasurementController] so it can
 * be unit-tested on the plain JVM with fakes.
 */
class MeasurementViewModel(app: Application) : AndroidViewModel(app) {

    private val database = TmsDatabase.getInstance(app)

    private val repository = TmsRepository(
        measurementDao = database.measurementDao(),
        treatmentDao = database.treatmentDao()
    )

    private val controller = MeasurementController(
        repository = repository,
        importer = LegacyImporter(
            source = StorageLegacySource(MeasurementStorage(app)),
            writer = repository,
            flag = PrefsMigrationFlag(app)
        ),
        scope = viewModelScope,
        ioDispatcher = Dispatchers.IO,
        treatments = repository
    )

    val measurements: StateFlow<List<MeasurementEntity>> = controller.measurements

    fun addMeasurement(
        clientName: String,
        tttCm: Double,
        niCm: Double,
        hcCm: Double,
        xCm: Double,
        yCm: Double,
        yAdjCm: Double
    ) {
        val timestamp = System.currentTimeMillis()
        controller.addMeasurement(
            MeasurementEntity(
                id = timestamp.toString(),
                clientName = clientName,
                tttCm = tttCm,
                niCm = niCm,
                hcCm = hcCm,
                xCm = xCm,
                yCm = yCm,
                yAdjCm = yAdjCm,
                timestampMs = timestamp
            )
        )
    }

    fun deleteMeasurement(id: String) = controller.deleteMeasurement(id)

    /** Live treatment list for one measurement (newest first), backed by Room. */
    fun treatmentsFor(measurementId: String): StateFlow<List<TreatmentEntity>> =
        controller.treatmentsFor(measurementId)

    fun saveTreatment(entity: TreatmentEntity) = controller.saveTreatment(entity)

    fun deleteTreatment(entity: TreatmentEntity) = controller.deleteTreatment(entity)

    /** Loads a single treatment for edit prefill and delivers it on the main thread. */
    fun getTreatment(id: Long, onResult: (TreatmentEntity?) -> Unit) {
        viewModelScope.launch {
            onResult(controller.getTreatment(id))
        }
    }
}
