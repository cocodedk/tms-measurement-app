package com.cocode.tmsmeasurement.ui.measurement

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cocode.tmsmeasurement.MeasurementRecord
import com.cocode.tmsmeasurement.R
import com.cocode.tmsmeasurement.viewmodel.MeasurementViewModel

/** Resolves the measurement validation messages from string resources for the screen. */
@Composable
internal fun rememberMeasurementErrors(): MeasurementErrorMessages =
    MeasurementErrorMessages(
        clientRequired = stringResource(R.string.error_client_required),
        tttPositive = stringResource(R.string.error_ttt_positive),
        niPositive = stringResource(R.string.error_ni_positive),
        hcPositive = stringResource(R.string.error_hc_positive)
    )

/**
 * Validates the raw measurement inputs, and on success persists the record through
 * [viewModel] and reports it via [onSuccess]; on failure reports the message via
 * [onError]. Extracted from `MeasurementApp` to keep that file focused and small.
 */
internal fun calculateAndStore(
    clientName: String,
    tttInput: String,
    niInput: String,
    hcInput: String,
    errors: MeasurementErrorMessages,
    viewModel: MeasurementViewModel,
    nowMs: Long,
    onSuccess: (MeasurementRecord) -> Unit,
    onError: (String) -> Unit
) {
    val outcome = calculateMeasurement(
        clientName = clientName,
        tttInput = tttInput,
        niInput = niInput,
        hcInput = hcInput,
        errors = errors,
        nowMs = nowMs
    )
    when (outcome) {
        is MeasurementCalcOutcome.Error -> onError(outcome.message)
        is MeasurementCalcOutcome.Success -> {
            val record = outcome.record
            viewModel.addMeasurement(
                clientName = record.clientName,
                tttCm = record.tttCm,
                niCm = record.niCm,
                hcCm = record.hcCm,
                xCm = record.xCm,
                yCm = record.yCm,
                yAdjCm = record.yAdjCm
            )
            onSuccess(record)
        }
    }
}
