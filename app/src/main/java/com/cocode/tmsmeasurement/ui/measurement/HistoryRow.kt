package com.cocode.tmsmeasurement.ui.measurement

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
import com.cocode.tmsmeasurement.data.local.MeasurementEntity
import com.cocode.tmsmeasurement.ui.common.formatCm
import com.cocode.tmsmeasurement.ui.common.formatTimestamp

@Composable
internal fun HistoryRow(record: MeasurementEntity, onClick: () -> Unit) {
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
                text = record.clientName,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = formatTimestamp(record.timestampMs),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = stringResource(
                    R.string.history_summary,
                    formatCm(record.xCm),
                    formatCm(record.yCm),
                    formatCm(record.yAdjCm)
                ),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
