package com.cocode.tmsmeasurement.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cocode.tmsmeasurement.R
import com.cocode.tmsmeasurement.data.local.MeasurementEntity
import com.cocode.tmsmeasurement.ui.common.formatCm
import com.cocode.tmsmeasurement.ui.common.formatTimestamp

/** The measurement's value rows, reusing the existing dialog_* labels (DRY with the old detail). */
@Composable
internal fun MeasurementValueRows(measurement: MeasurementEntity) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(stringResource(R.string.dialog_ttt, formatCm(measurement.tttCm)))
        Text(stringResource(R.string.dialog_ni, formatCm(measurement.niCm)))
        Text(stringResource(R.string.dialog_hc, formatCm(measurement.hcCm)))
        Text(stringResource(R.string.dialog_x, formatCm(measurement.xCm)))
        Text(stringResource(R.string.dialog_y, formatCm(measurement.yCm)))
        Text(stringResource(R.string.dialog_y_adj, formatCm(measurement.yAdjCm)))
        Text(stringResource(R.string.dialog_recorded, formatTimestamp(measurement.timestampMs)))
    }
}

/** Confirm dialog for deleting a measurement, warning that its treatments cascade away. */
@Composable
internal fun DeleteMeasurementDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_delete_measurement_title)) },
        text = { Text(stringResource(R.string.dialog_delete_measurement_message)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.dialog_delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.dialog_cancel))
            }
        }
    )
}
