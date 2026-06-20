package com.cocode.tmsmeasurement.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cocode.tmsmeasurement.R
import com.cocode.tmsmeasurement.data.local.MeasurementEntity
import com.cocode.tmsmeasurement.data.local.TreatmentEntity

/**
 * Detail view for one saved measurement: its calculated values, the treatment
 * sessions attached to it (newest first), an "Add treatment" action, and a
 * "Delete measurement" action guarded by a confirm dialog that warns the
 * treatments are removed too (foreign-key cascade).
 */
@Composable
internal fun MeasurementDetailScreen(
    innerPadding: PaddingValues,
    measurement: MeasurementEntity,
    treatments: List<TreatmentEntity>,
    onAddTreatment: () -> Unit,
    onTreatmentClick: (Long) -> Unit,
    onDeleteMeasurement: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { MeasurementValuesCard(measurement) }

        item {
            Text(
                text = stringResource(R.string.section_treatments),
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (treatments.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.treatment_empty),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            items(treatments, key = { it.id }) { treatment ->
                TreatmentRow(
                    treatment = treatment,
                    onClick = { onTreatmentClick(treatment.id) }
                )
            }
        }

        item {
            Button(
                onClick = onAddTreatment,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.button_add_treatment))
            }
        }

        item {
            OutlinedButton(
                onClick = { showDeleteConfirm = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.button_delete_measurement),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    if (showDeleteConfirm) {
        DeleteMeasurementDialog(
            onConfirm = {
                showDeleteConfirm = false
                onDeleteMeasurement()
            },
            onDismiss = { showDeleteConfirm = false }
        )
    }
}

@Composable
private fun MeasurementValuesCard(measurement: MeasurementEntity) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = measurement.clientName,
                style = MaterialTheme.typography.titleMedium
            )
            MeasurementValueRows(measurement)
        }
    }
}
