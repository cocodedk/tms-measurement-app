package com.cocode.tmsmeasurement.ui.measurement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cocode.tmsmeasurement.MeasurementRecord
import com.cocode.tmsmeasurement.R
import com.cocode.tmsmeasurement.ui.common.formatCm

@Composable
internal fun ResultSection(record: MeasurementRecord) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.section_latest_result),
                style = MaterialTheme.typography.titleMedium
            )
            Text(stringResource(R.string.result_x, formatCm(record.xCm)))
            Text(stringResource(R.string.result_y, formatCm(record.yCm)))
            Text(stringResource(R.string.result_y_adj, formatCm(record.yAdjCm)))
            Text(
                text = stringResource(R.string.result_adjustment_note),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
