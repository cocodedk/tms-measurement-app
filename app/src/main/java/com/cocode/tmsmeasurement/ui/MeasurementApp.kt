package com.cocode.tmsmeasurement.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cocode.tmsmeasurement.MeasurementRecord
import com.cocode.tmsmeasurement.ui.about.AboutScreen
import com.cocode.tmsmeasurement.ui.common.SYSTEM_LANGUAGE_TAG
import com.cocode.tmsmeasurement.ui.common.normalizeLanguageTag
import com.cocode.tmsmeasurement.ui.common.openHelpPage
import com.cocode.tmsmeasurement.ui.detail.MeasurementDetailDestination
import com.cocode.tmsmeasurement.ui.measurement.MeasurementScreen
import com.cocode.tmsmeasurement.ui.measurement.calculateAndStore
import com.cocode.tmsmeasurement.ui.measurement.rememberMeasurementErrors
import com.cocode.tmsmeasurement.ui.settings.SettingsScreen
import com.cocode.tmsmeasurement.ui.theme.TMSMeasurementTheme
import com.cocode.tmsmeasurement.ui.treatment.TreatmentFormDestination
import com.cocode.tmsmeasurement.viewmodel.MeasurementViewModel

enum class AppScreen {
    Measurement,
    About,
    Settings,
    MeasurementDetail,
    TreatmentForm
}

@Composable
fun MeasurementApp() {
    val context = LocalContext.current
    val activity = context as? Activity
    val viewModel: MeasurementViewModel = viewModel()
    val records by viewModel.measurements.collectAsStateWithLifecycle()
    var lastResult by remember { mutableStateOf<MeasurementRecord?>(null) }
    var selectedMeasurementId by rememberSaveable { mutableStateOf<String?>(null) }
    var editingTreatmentId by rememberSaveable { mutableStateOf<Long?>(null) }

    var clientName by rememberSaveable { mutableStateOf("") }
    var tttInput by rememberSaveable { mutableStateOf("") }
    var niInput by rememberSaveable { mutableStateOf("") }
    var hcInput by rememberSaveable { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var screen by rememberSaveable { mutableStateOf(AppScreen.Measurement) }
    val errorMessages = rememberMeasurementErrors()
    val currentLanguageTag = normalizeLanguageTag(
        AppCompatDelegate.getApplicationLocales().toLanguageTags()
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MeasurementTopBar(
                screen = screen,
                isEditingTreatment = editingTreatmentId != null,
                onBack = {
                    // The form returns to the measurement detail it was opened from;
                    // every other screen returns to the measurement list.
                    if (screen == AppScreen.TreatmentForm) {
                        editingTreatmentId = null
                        screen = AppScreen.MeasurementDetail
                    } else {
                        screen = AppScreen.Measurement
                        selectedMeasurementId = null
                        editingTreatmentId = null
                    }
                },
                onSettings = { screen = AppScreen.Settings },
                onHelp = { openHelpPage(context) },
                onAbout = { screen = AppScreen.About }
            )
        }
    ) { innerPadding ->
        when (screen) {
            AppScreen.About -> AboutScreen(innerPadding = innerPadding)
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
            AppScreen.MeasurementDetail -> {
                MeasurementDetailDestination(
                    innerPadding = innerPadding,
                    measurementId = selectedMeasurementId,
                    records = records,
                    viewModel = viewModel,
                    // Open the form to add (null) or edit (the row's id) a treatment.
                    onOpenTreatmentForm = { id ->
                        editingTreatmentId = id
                        screen = AppScreen.TreatmentForm
                    },
                    onDeleted = { id ->
                        viewModel.deleteMeasurement(id)
                        selectedMeasurementId = null
                        screen = AppScreen.Measurement
                    },
                    onBack = {
                        selectedMeasurementId = null
                        screen = AppScreen.Measurement
                    }
                )
            }
            AppScreen.TreatmentForm -> {
                TreatmentFormDestination(
                    innerPadding = innerPadding,
                    measurementId = selectedMeasurementId,
                    editingTreatmentId = editingTreatmentId,
                    nowMs = System.currentTimeMillis(),
                    viewModel = viewModel,
                    onDone = {
                        editingTreatmentId = null
                        screen = AppScreen.MeasurementDetail
                    },
                    onMissing = {
                        editingTreatmentId = null
                        selectedMeasurementId = null
                        screen = AppScreen.Measurement
                    }
                )
            }
            else -> {
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
                    onRecordClick = {
                        selectedMeasurementId = it.id
                        screen = AppScreen.MeasurementDetail
                    },
                    onCalculate = {
                        calculateAndStore(
                            clientName = clientName,
                            tttInput = tttInput,
                            niInput = niInput,
                            hcInput = hcInput,
                            errors = errorMessages,
                            viewModel = viewModel,
                            nowMs = System.currentTimeMillis(),
                            onSuccess = { record ->
                                lastResult = record
                                errorMessage = null
                            },
                            onError = { errorMessage = it }
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MeasurementPreview() {
    TMSMeasurementTheme {
        MeasurementApp()
    }
}
