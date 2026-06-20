package com.cocode.tmsmeasurement.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cocode.tmsmeasurement.R

/**
 * Top app bar for [MeasurementApp]. The title reflects the current [screen] (and add-vs-edit for
 * the treatment form), a Back action shows on every non-root screen, and the list-only actions
 * (settings, help, about) appear only on the measurement screen. All navigation is hoisted.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MeasurementTopBar(
    screen: AppScreen,
    isEditingTreatment: Boolean,
    onBack: () -> Unit,
    onSettings: () -> Unit,
    onHelp: () -> Unit,
    onAbout: () -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(titleResId(screen, isEditingTreatment))) },
        navigationIcon = {
            if (screen != AppScreen.Measurement) {
                TextButton(onClick = onBack) {
                    Text(stringResource(R.string.action_back))
                }
            }
        },
        actions = {
            if (screen == AppScreen.Measurement) {
                TextButton(onClick = onSettings) {
                    Text(stringResource(R.string.action_settings))
                }
                TextButton(onClick = onHelp) {
                    Text(stringResource(R.string.action_help))
                }
                TextButton(onClick = onAbout) {
                    Text(stringResource(R.string.action_about))
                }
            }
        }
    )
}

private fun titleResId(screen: AppScreen, isEditingTreatment: Boolean): Int = when (screen) {
    AppScreen.About -> R.string.screen_title_about
    AppScreen.Settings -> R.string.screen_title_settings
    AppScreen.MeasurementDetail -> R.string.section_measurement
    AppScreen.TreatmentForm ->
        if (isEditingTreatment) R.string.screen_title_treatment_edit
        else R.string.screen_title_treatment_add
    AppScreen.Measurement -> R.string.screen_title_measurement
}
