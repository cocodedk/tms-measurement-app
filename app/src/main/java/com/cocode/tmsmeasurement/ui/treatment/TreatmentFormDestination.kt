package com.cocode.tmsmeasurement.ui.treatment

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.cocode.tmsmeasurement.viewmodel.MeasurementViewModel

/**
 * Navigation destination wrapper for the treatment form, mirroring
 * `MeasurementDetailDestination`. Guards the required [measurementId] (asking the host to pop
 * back via [onMissing] if it is gone) and binds the form's callbacks to the ViewModel so
 * `MeasurementApp` stays small.
 */
@Composable
internal fun TreatmentFormDestination(
    innerPadding: PaddingValues,
    measurementId: String?,
    editingTreatmentId: Long?,
    nowMs: Long,
    viewModel: MeasurementViewModel,
    onDone: () -> Unit,
    onMissing: () -> Unit
) {
    if (measurementId == null) {
        LaunchedEffect(Unit) { onMissing() }
        return
    }

    TreatmentFormScreen(
        innerPadding = innerPadding,
        measurementId = measurementId,
        editingTreatmentId = editingTreatmentId,
        nowMs = nowMs,
        loadTreatment = viewModel::getTreatment,
        onSave = viewModel::saveTreatment,
        onDelete = viewModel::deleteTreatment,
        onDone = onDone
    )
}
