package com.cocode.tmsmeasurement.ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cocode.tmsmeasurement.R
import com.cocode.tmsmeasurement.data.local.TreatmentEntity
import com.cocode.tmsmeasurement.ui.common.formatDate

@Composable
internal fun TreatmentRow(treatment: TreatmentEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = formatDate(treatment.dateMs),
                style = MaterialTheme.typography.titleSmall
            )
            val summary = treatmentSummaryLine(treatment)
            Text(
                text = summary ?: stringResource(R.string.treatment_row_more),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/** Joins the present clinical values into a localized one-line summary, or null when none. */
@Composable
private fun treatmentSummaryLine(treatment: TreatmentEntity): String? {
    val parts = treatmentSummaryChips(treatment).map { chip ->
        when (chip.type) {
            TreatmentChipType.INTENSITY ->
                stringResource(R.string.treatment_chip_intensity, chip.value)
            TreatmentChipType.TOTAL_PULSES ->
                stringResource(R.string.treatment_chip_total_pulses, chip.value)
            TreatmentChipType.FREQUENCY ->
                stringResource(R.string.treatment_chip_frequency, chip.value)
            TreatmentChipType.MOTOR_THRESHOLD ->
                stringResource(R.string.treatment_chip_motor_threshold, chip.value)
        }
    }
    val separator = stringResource(R.string.treatment_chip_separator)
    return parts.takeIf { it.isNotEmpty() }?.joinToString(separator = separator)
}
