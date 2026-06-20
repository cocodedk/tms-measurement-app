package com.cocode.tmsmeasurement.ui.measurement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.cocode.tmsmeasurement.R

@Composable
internal fun InputSection(
    clientName: String,
    onClientNameChange: (String) -> Unit,
    tttInput: String,
    onTttChange: (String) -> Unit,
    niInput: String,
    onNiChange: (String) -> Unit,
    hcInput: String,
    onHcChange: (String) -> Unit,
    errorMessage: String?,
    onCalculate: () -> Unit
) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.section_client_measurements),
                style = MaterialTheme.typography.titleMedium
            )
            OutlinedTextField(
                value = clientName,
                onValueChange = onClientNameChange,
                label = { Text(stringResource(R.string.label_client_name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = tttInput,
                onValueChange = onTttChange,
                label = { Text(stringResource(R.string.label_ttt)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                suffix = { Text(stringResource(R.string.unit_cm)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            OutlinedTextField(
                value = niInput,
                onValueChange = onNiChange,
                label = { Text(stringResource(R.string.label_ni)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                suffix = { Text(stringResource(R.string.unit_cm)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            OutlinedTextField(
                value = hcInput,
                onValueChange = onHcChange,
                label = { Text(stringResource(R.string.label_hc)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                suffix = { Text(stringResource(R.string.unit_cm)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Button(
                onClick = onCalculate,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.button_calculate_save))
            }
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
