package com.cocode.tmsmeasurement

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.cocode.tmsmeasurement.BuildConfig
import com.cocode.tmsmeasurement.ui.theme.TMSMeasurementTheme
import java.text.DateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {
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
    val activity = context as? Activity
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
    var screen by rememberSaveable { mutableStateOf(AppScreen.Measurement) }
    val errorClientRequired = stringResource(R.string.error_client_required)
    val errorTttPositive = stringResource(R.string.error_ttt_positive)
    val errorNiPositive = stringResource(R.string.error_ni_positive)
    val errorHcPositive = stringResource(R.string.error_hc_positive)
    val currentLanguageTag = normalizeLanguageTag(
        AppCompatDelegate.getApplicationLocales().toLanguageTags()
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (screen) {
                            AppScreen.Measurement -> stringResource(R.string.screen_title_measurement)
                            AppScreen.About -> stringResource(R.string.screen_title_about)
                            AppScreen.Settings -> stringResource(R.string.screen_title_settings)
                        }
                    )
                },
                navigationIcon = {
                    if (screen != AppScreen.Measurement) {
                        TextButton(onClick = { screen = AppScreen.Measurement }) {
                            Text(stringResource(R.string.action_back))
                        }
                    }
                },
                actions = {
                    if (screen == AppScreen.Measurement) {
                        TextButton(onClick = { screen = AppScreen.Settings }) {
                            Text(stringResource(R.string.action_settings))
                        }
                        TextButton(onClick = { screen = AppScreen.About }) {
                            Text(stringResource(R.string.action_about))
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        when (screen) {
            AppScreen.Measurement -> {
                MeasurementScreen(
                    innerPadding = innerPadding,
                    clientName = clientName,
                    onClientNameChange = { clientName = it },
                    tttInput = tttInput,
                    onTttChange = { tttInput = it },
                    niInput = niInput,
                    onNiChange = { niInput = it },
                    hcInput = hcInput,
                    onHcChange = { hcInput = it },
                    errorMessage = errorMessage,
                    lastResult = lastResult,
                    records = records,
                    onRecordClick = { selectedRecord = it },
                    onCalculate = {
                        val trimmedName = clientName.trim()
                        val ttt = tttInput.toDoubleOrNull()
                        val ni = niInput.toDoubleOrNull()
                        val hc = hcInput.toDoubleOrNull()

                        val validationError = when {
                            trimmedName.isEmpty() -> errorClientRequired
                            ttt == null || ttt <= 0 -> errorTttPositive
                            ni == null || ni <= 0 -> errorNiPositive
                            hc == null || hc <= 0 -> errorHcPositive
                            else -> null
                        }

                        if (validationError != null) {
                            errorMessage = validationError
                        } else {
                            val timestamp = System.currentTimeMillis()
                            val tttValue = requireNotNull(ttt)
                            val niValue = requireNotNull(ni)
                            val hcValue = requireNotNull(hc)
                            val result = BeamF3Calculator.calculate(tttValue, niValue, hcValue)
                            val computed = MeasurementRecord(
                                id = timestamp.toString(),
                                clientName = trimmedName,
                                tttCm = BeamF3Calculator.roundToTwo(tttValue),
                                niCm = BeamF3Calculator.roundToTwo(niValue),
                                hcCm = BeamF3Calculator.roundToTwo(hcValue),
                                xCm = result.xCm,
                                yCm = result.yCm,
                                yAdjCm = result.yAdjCm,
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
            AppScreen.About -> {
                AboutScreen(innerPadding = innerPadding)
            }
            AppScreen.Settings -> {
                SettingsScreen(
                    innerPadding = innerPadding,
                    currentLanguageTag = currentLanguageTag,
                    onLanguageSelected = { selectedTag ->
                        val locales = if (selectedTag == SYSTEM_LANGUAGE_TAG) {
                            LocaleListCompat.getEmptyLocaleList()
                        } else {
                            LocaleListCompat.forLanguageTags(selectedTag)
                        }
                        AppCompatDelegate.setApplicationLocales(locales)
                        activity?.recreate()
                    }
                )
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
                    Text(stringResource(R.string.dialog_ttt, formatCm(record.tttCm)))
                    Text(stringResource(R.string.dialog_ni, formatCm(record.niCm)))
                    Text(stringResource(R.string.dialog_hc, formatCm(record.hcCm)))
                    Text(stringResource(R.string.dialog_x, formatCm(record.xCm)))
                    Text(stringResource(R.string.dialog_y, formatCm(record.yCm)))
                    Text(stringResource(R.string.dialog_y_adj, formatCm(record.yAdjCm)))
                    Text(stringResource(R.string.dialog_recorded, formatTimestamp(record.timestampMs)))
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
                    Text(stringResource(R.string.dialog_delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedRecord = null }) {
                    Text(stringResource(R.string.dialog_close))
                }
            }
        )
    }
}

private enum class AppScreen {
    Measurement,
    About,
    Settings
}

@Composable
private fun MeasurementScreen(
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
    records: List<MeasurementRecord>,
    onRecordClick: (MeasurementRecord) -> Unit,
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

@Composable
private fun AboutScreen(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.screen_title_about),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.about_body),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(
                        R.string.about_version,
                        BuildConfig.VERSION_NAME,
                        BuildConfig.VERSION_CODE
                    ),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = stringResource(R.string.about_created_by),
                    style = MaterialTheme.typography.titleMedium
                )
                Text("Babak Bandpey")
                Text("bb@cocode.dk")
                Text("https://cocode.dk")
            }
        }
    }
}

@Composable
private fun SettingsScreen(
    innerPadding: PaddingValues,
    currentLanguageTag: String,
    onLanguageSelected: (String) -> Unit
) {
    val languageOptions = listOf(
        LanguageOption(SYSTEM_LANGUAGE_TAG, R.string.language_system),
        LanguageOption("en", R.string.language_english),
        LanguageOption("fa", R.string.language_persian),
        LanguageOption("ar", R.string.language_arabic),
        LanguageOption("zh-TW", R.string.language_chinese_taiwan)
    )
    var selectedTag by rememberSaveable(currentLanguageTag) { mutableStateOf(currentLanguageTag) }
    val hasChanges = selectedTag != currentLanguageTag

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.settings_language_title),
                    style = MaterialTheme.typography.titleMedium
                )
                languageOptions.forEach { option ->
                    val isSelected = selectedTag == option.tag
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedTag = option.tag
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = {
                                selectedTag = option.tag
                            }
                        )
                        Text(
                            text = stringResource(option.labelRes),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                Button(
                    onClick = { onLanguageSelected(selectedTag) },
                    enabled = hasChanges,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.button_apply_language))
                }
            }
        }
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

@Composable
private fun ResultSection(record: MeasurementRecord) {
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

@Composable
private fun formatCm(value: Double): String {
    val unit = stringResource(R.string.unit_cm)
    return stringResource(R.string.value_cm_format, value, unit)
}

private fun formatTimestamp(timestampMs: Long): String {
    val formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
    return formatter.format(Date(timestampMs))
}

private data class LanguageOption(
    val tag: String,
    val labelRes: Int
)

private const val SYSTEM_LANGUAGE_TAG = "system"

private fun normalizeLanguageTag(rawTags: String): String {
    val tag = rawTags.split(',').firstOrNull()?.trim().orEmpty()
    if (tag.isBlank()) {
        return SYSTEM_LANGUAGE_TAG
    }
    val lower = tag.lowercase()
    return when {
        lower.startsWith("fa") -> "fa"
        lower.startsWith("ar") -> "ar"
        lower == "zh-tw" || lower.startsWith("zh-hant") -> "zh-TW"
        lower.startsWith("en") -> "en"
        else -> SYSTEM_LANGUAGE_TAG
    }
}

@Preview(showBackground = true)
@Composable
fun MeasurementPreview() {
    TMSMeasurementTheme {
        MeasurementApp()
    }
}
