package com.cocode.tmsmeasurement.ui.detail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cocode.tmsmeasurement.data.local.MeasurementEntity
import com.cocode.tmsmeasurement.viewmodel.MeasurementViewModel

/**
 * Stateful host for [MeasurementDetailScreen]: resolves the selected measurement from
 * the observed list, subscribes to its treatments (newest first), and forwards add /
 * edit / delete intents. If the id is missing or the measurement is gone, it asks the
 * caller to navigate back via [onMissing] rather than rendering a broken screen.
 */
@Composable
internal fun MeasurementDetailRoute(
    innerPadding: PaddingValues,
    measurementId: String?,
    records: List<MeasurementEntity>,
    viewModel: MeasurementViewModel,
    onAddTreatment: () -> Unit,
    onTreatmentClick: (Long) -> Unit,
    onDeleted: (String) -> Unit,
    onMissing: () -> Unit
) {
    val measurement = measurementId?.let { id -> records.firstOrNull { it.id == id } }

    if (measurement == null) {
        LaunchedEffect(measurementId) { onMissing() }
        return
    }

    val treatments by viewModel.treatmentsFor(measurement.id).collectAsStateWithLifecycle()

    MeasurementDetailScreen(
        innerPadding = innerPadding,
        measurement = measurement,
        treatments = treatments,
        onAddTreatment = onAddTreatment,
        onTreatmentClick = onTreatmentClick,
        onDeleteMeasurement = { onDeleted(measurement.id) }
    )
}
