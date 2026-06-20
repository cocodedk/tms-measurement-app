package com.cocode.tmsmeasurement.ui.detail

import com.cocode.tmsmeasurement.data.local.TreatmentEntity

/** The clinical values shown as a compact summary on a treatment row, in priority order. */
enum class TreatmentChipType { INTENSITY, TOTAL_PULSES, FREQUENCY, MOTOR_THRESHOLD }

/**
 * One summary item: which value it is and its plain number text. The text is a bare
 * number (e.g. "120" or "55.5"), never a user-facing label — the Composable wraps it
 * in a localized string resource so nothing is hardcoded.
 */
data class TreatmentChip(val type: TreatmentChipType, val value: String)

/**
 * Pure selection of the most relevant non-null clinical values on [entity], in a fixed
 * priority order and capped at [maxChips]. No Android types, no formatting locale — keeps
 * the treatment row's logic unit-testable on the plain JVM.
 */
fun treatmentSummaryChips(entity: TreatmentEntity, maxChips: Int = 3): List<TreatmentChip> =
    buildList {
        entity.intensityPctMt?.let { add(TreatmentChip(TreatmentChipType.INTENSITY, trimDouble(it))) }
        entity.totalPulses?.let { add(TreatmentChip(TreatmentChipType.TOTAL_PULSES, it.toString())) }
        entity.frequencyHz?.let { add(TreatmentChip(TreatmentChipType.FREQUENCY, trimDouble(it))) }
        entity.motorThresholdPctMso?.let {
            add(TreatmentChip(TreatmentChipType.MOTOR_THRESHOLD, trimDouble(it)))
        }
    }.take(maxChips)

/** Renders a Double as a compact string: whole numbers drop the ".0", decimals are kept. */
private fun trimDouble(value: Double): String =
    if (value % 1.0 == 0.0) value.toLong().toString() else value.toString()
