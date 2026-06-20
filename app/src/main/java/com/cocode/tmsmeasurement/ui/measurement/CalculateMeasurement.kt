package com.cocode.tmsmeasurement.ui.measurement

import com.cocode.tmsmeasurement.BeamF3Calculator
import com.cocode.tmsmeasurement.MeasurementRecord

/** Pre-resolved validation messages, so this stays free of Compose/Android. */
data class MeasurementErrorMessages(
    val clientRequired: String,
    val tttPositive: String,
    val niPositive: String,
    val hcPositive: String
)

/** Outcome of attempting to calculate and save: an error, or a computed record. */
sealed interface MeasurementCalcOutcome {
    data class Error(val message: String) : MeasurementCalcOutcome
    data class Success(val record: MeasurementRecord) : MeasurementCalcOutcome
}

/**
 * Pure validate-and-calculate for the measurement form. Trims the name, parses the
 * three inputs, validates them positive, and (on success) builds the [MeasurementRecord]
 * using [nowMs] as both id and timestamp — identical to the inline logic it replaces.
 */
fun calculateMeasurement(
    clientName: String,
    tttInput: String,
    niInput: String,
    hcInput: String,
    errors: MeasurementErrorMessages,
    nowMs: Long
): MeasurementCalcOutcome {
    val trimmedName = clientName.trim()
    val ttt = tttInput.toDoubleOrNull()
    val ni = niInput.toDoubleOrNull()
    val hc = hcInput.toDoubleOrNull()

    val validationError = when {
        trimmedName.isEmpty() -> errors.clientRequired
        ttt == null || ttt <= 0 -> errors.tttPositive
        ni == null || ni <= 0 -> errors.niPositive
        hc == null || hc <= 0 -> errors.hcPositive
        else -> null
    }
    if (validationError != null) {
        return MeasurementCalcOutcome.Error(validationError)
    }

    val tttValue = requireNotNull(ttt)
    val niValue = requireNotNull(ni)
    val hcValue = requireNotNull(hc)
    val result = BeamF3Calculator.calculate(tttValue, niValue, hcValue)
    val record = MeasurementRecord(
        id = nowMs.toString(),
        clientName = trimmedName,
        tttCm = BeamF3Calculator.roundToTwo(tttValue),
        niCm = BeamF3Calculator.roundToTwo(niValue),
        hcCm = BeamF3Calculator.roundToTwo(hcValue),
        xCm = result.xCm,
        yCm = result.yCm,
        yAdjCm = result.yAdjCm,
        timestampMs = nowMs
    )
    return MeasurementCalcOutcome.Success(record)
}
