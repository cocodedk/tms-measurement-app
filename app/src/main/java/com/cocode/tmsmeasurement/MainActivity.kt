package com.cocode.tmsmeasurement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import com.cocode.tmsmeasurement.ui.theme.TMSMeasurementTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.round

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TMSMeasurementTheme {
                MeasurementApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeasurementApp() {
    val context = LocalContext.current
    val storage = remember { MeasurementStorage(context) }
    var records by remember {
        mutableStateOf(storage.load().sortedByDescending { it.timestampMs })
    }
    var lastResult by remember { mutableStateOf<MeasurementRecord?>(null) }
    var selectedRecord by remember { mutableStateOf<MeasurementRecord?>(null) }

    var clientName by rememberSaveable { mutableStateOf("") }
    var tttInput by rememberSaveable { mutableStateOf("") }
    var niInput by rememberSaveable { mutableStateOf("") }
    var hcInput by rememberSaveable { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text("TMS F3 Measurement") }) }
    ) { innerPadding ->
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
                    onClientNameChange = { clientName = it },
                    tttInput = tttInput,
                    onTttChange = { tttInput = it },
                    niInput = niInput,
                    onNiChange = { niInput = it },
                    hcInput = hcInput,
                    onHcChange = { hcInput = it },
                    errorMessage = errorMessage,
                    onCalculate = {
                        val trimmedName = clientName.trim()
                        val ttt = tttInput.toDoubleOrNull()
                        val ni = niInput.toDoubleOrNull()
                        val hc = hcInput.toDoubleOrNull()

                        val validationError = when {
                            trimmedName.isEmpty() -> "Client name is required."
                            ttt == null || ttt <= 0 -> "Tragus to tragus must be a positive number."
                            ni == null || ni <= 0 -> "Nasion to inion must be a positive number."
                            hc == null || hc <= 0 -> "Head circumference must be a positive number."
                            else -> null
                        }

                        if (validationError != null) {
                            errorMessage = validationError
                        } else {
                            val timestamp = System.currentTimeMillis()
                            val tttValue = requireNotNull(ttt)
                            val niValue = requireNotNull(ni)
                            val hcValue = requireNotNull(hc)
                            val avg = (tttValue + niValue) / 2.0
                            val rawY = 0.2637 * avg
                            val computed = MeasurementRecord(
                                id = timestamp.toString(),
                                clientName = trimmedName,
                                tttCm = roundToTwo(tttValue),
                                niCm = roundToTwo(niValue),
                                hcCm = roundToTwo(hcValue),
                                xCm = roundToTwo(0.1154 * hcValue),
                                yCm = roundToTwo(rawY),
                                yAdjCm = roundToTwo(rawY + 0.35),
                                timestampMs = timestamp
                            )
                            val updated = listOf(computed) + records
                            records = updated
                            storage.saveAll(updated)
                            lastResult = computed
                            errorMessage = null
                        }
                    }
                )
            }

            if (lastResult != null) {
                item {
                    ResultSection(record = lastResult!!)
                }
            }

            item {
                Text(
                    text = "History",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (records.isEmpty()) {
                item {
                    Text(
                        text = "No saved measurements yet.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                items(records, key = { it.id }) { record ->
                    HistoryRow(
                        record = record,
                        onClick = { selectedRecord = record }
                    )
                }
            }
        }
    }

    if (selectedRecord != null) {
        val record = selectedRecord!!
        AlertDialog(
            onDismissRequest = { selectedRecord = null },
            title = { Text(record.clientName) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Tragus to tragus: ${formatCm(record.tttCm)}")
                    Text("Nasion to inion: ${formatCm(record.niCm)}")
                    Text("Head circumference: ${formatCm(record.hcCm)}")
                    Text("X (circumference distance): ${formatCm(record.xCm)}")
                    Text("Y (vertex distance): ${formatCm(record.yCm)}")
                    Text("Adjusted Y: ${formatCm(record.yAdjCm)}")
                    Text("Recorded: ${formatTimestamp(record.timestampMs)}")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val updated = records.filterNot { it.id == record.id }
                        records = updated
                        storage.saveAll(updated)
                        selectedRecord = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedRecord = null }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun InputSection(
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
                text = "Client and measurements",
                style = MaterialTheme.typography.titleMedium
            )
            OutlinedTextField(
                value = clientName,
                onValueChange = onClientNameChange,
                label = { Text("Client name or ID") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = tttInput,
                onValueChange = onTttChange,
                label = { Text("Tragus to tragus") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                suffix = { Text("cm") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            OutlinedTextField(
                value = niInput,
                onValueChange = onNiChange,
                label = { Text("Nasion to inion") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                suffix = { Text("cm") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            OutlinedTextField(
                value = hcInput,
                onValueChange = onHcChange,
                label = { Text("Head circumference") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                suffix = { Text("cm") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Button(
                onClick = onCalculate,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calculate and save")
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

@Composable
private fun ResultSection(record: MeasurementRecord) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Latest result",
                style = MaterialTheme.typography.titleMedium
            )
            Text("Distance along circumference (X): ${formatCm(record.xCm)}")
            Text("Distance from vertex (Y): ${formatCm(record.yCm)}")
            Text("Adjusted Y: ${formatCm(record.yAdjCm)}")
            Text(
                text = "Adjusted Y includes a 0.35 cm correction to align with MRI-guided targeting.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun HistoryRow(record: MeasurementRecord, onClick: () -> Unit) {
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
                text = "X ${formatCm(record.xCm)} | Y ${formatCm(record.yCm)} | Adj ${formatCm(record.yAdjCm)}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun formatCm(value: Double): String =
    String.format(Locale.US, "%.2f cm", value)

private fun formatTimestamp(timestampMs: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
    return formatter.format(Date(timestampMs))
}

private fun roundToTwo(value: Double): Double = round(value * 100) / 100

@Preview(showBackground = true)
@Composable
fun MeasurementPreview() {
    TMSMeasurementTheme {
        MeasurementApp()
    }
}
