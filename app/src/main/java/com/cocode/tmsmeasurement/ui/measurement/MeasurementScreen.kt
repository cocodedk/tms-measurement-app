package com.cocode.tmsmeasurement.ui.measurement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cocode.tmsmeasurement.MeasurementRecord
import com.cocode.tmsmeasurement.R
import com.cocode.tmsmeasurement.data.local.MeasurementEntity

@Composable
internal fun MeasurementScreen(
    innerPadding: PaddingValues,
    clientName: String,
    onClientNameChange: (String) -> Unit,
    tttInput: String,
    onTttChange: (String) -> Unit,
    niInput: String,
    onNiChange: (String) -> Unit,
    hcInput: String,
    onHcChange: (String) -> Unit,
    errorMessage: String?,
    lastResult: MeasurementRecord?,
    records: List<MeasurementEntity>,
    onRecordClick: (MeasurementEntity) -> Unit,
    onCalculate: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            InputSection(
                clientName = clientName,
                onClientNameChange = onClientNameChange,
                tttInput = tttInput,
                onTttChange = onTttChange,
                niInput = niInput,
                onNiChange = onNiChange,
                hcInput = hcInput,
                onHcChange = onHcChange,
                errorMessage = errorMessage,
                onCalculate = onCalculate
            )
        }

        if (lastResult != null) {
            item {
                ResultSection(record = lastResult)
            }
        }

        item {
            Text(
                text = stringResource(R.string.section_history),
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (records.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.empty_history),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            items(records, key = { it.id }) { record ->
                HistoryRow(
                    record = record,
                    onClick = { onRecordClick(record) }
                )
            }
        }
    }
}
