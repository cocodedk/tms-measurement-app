package com.cocode.tmsmeasurement.ui.detail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.cocode.tmsmeasurement.data.local.MeasurementEntity
import com.cocode.tmsmeasurement.viewmodel.MeasurementViewModel

/**
 * Navigation destination wrapper for the measurement detail view. Keeps the
 * branch wiring out of `MeasurementApp` so that file stays small and focused.
 *
 * [onOpenTreatmentForm] is called with `null` to add a treatment or the row id to
 * edit one; the host translates that into its hoisted nav state. [onDeleted] and
 * [onBack] let the host pop back to the measurement list.
 */
@Composable
internal fun MeasurementDetailDestination(
    innerPadding: PaddingValues,
    measurementId: String?,
    records: List<MeasurementEntity>,
    viewModel: MeasurementViewModel,
    onOpenTreatmentForm: (Long?) -> Unit,
    onDeleted: (String) -> Unit,
    onBack: () -> Unit
) {
    MeasurementDetailRoute(
        innerPadding = innerPadding,
        measurementId = measurementId,
        records = records,
        viewModel = viewModel,
        onAddTreatment = { onOpenTreatmentForm(null) },
        onTreatmentClick = { id -> onOpenTreatmentForm(id) },
        onDeleted = onDeleted,
        onMissing = onBack
    )
}
