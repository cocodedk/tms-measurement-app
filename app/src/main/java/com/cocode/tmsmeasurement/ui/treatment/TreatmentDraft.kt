package com.cocode.tmsmeasurement.ui.treatment

import androidx.annotation.StringRes
import com.cocode.tmsmeasurement.R
import com.cocode.tmsmeasurement.data.local.TreatmentEntity

/** Identifies which form field an error belongs to, so the UI can highlight it. */
enum class TreatmentField {
    INTENSITY,
    TOTAL_PULSES,
    FREQUENCY,
    MOTOR_THRESHOLD,
    NUMBER_OF_TRAINS,
    PULSES_PER_TRAIN,
    TRAIN_DURATION,
    INTER_TRAIN_INTERVAL,
    SESSION_DURATION
}

/** A single validation failure, referencing a string-resource id rather than a literal message. */
data class TreatmentFieldError(
    val field: TreatmentField,
    @param:StringRes val messageResId: Int
)

/** Outcome of validating a [TreatmentDraft]: a ready-to-persist entity or the field errors. */
sealed interface TreatmentResult {
    data class Valid(val entity: TreatmentEntity) : TreatmentResult
    data class Invalid(val errors: List<TreatmentFieldError>) : TreatmentResult
}

/**
 * Immutable holder for the treatment form's raw string inputs. Pure: it knows nothing about
 * Android views or persistence. [toResult] parses and validates the strings into a
 * [TreatmentEntity], or collects every field error.
 *
 * Rules: the date (supplied to [toResult]) is required; all clinical fields are optional.
 * When a numeric field is non-blank it must parse and be greater than zero for the Double
 * fields and a non-negative integer for the Int fields. Blank optional fields become null.
 * Free-text fields (site, notes) are trimmed and become null when blank.
 */
data class TreatmentDraft(
    val intensity: String = "",
    val totalPulses: String = "",
    val frequency: String = "",
    val motorThreshold: String = "",
    val trains: String = "",
    val pulsesPerTrain: String = "",
    val trainDuration: String = "",
    val interTrainInterval: String = "",
    val site: String = "",
    val sessionDuration: String = "",
    val notes: String = ""
) {

    fun toResult(measurementId: String, dateMs: Long): TreatmentResult {
        val errors = mutableListOf<TreatmentFieldError>()

        val intensityValue = positiveDouble(
            intensity, TreatmentField.INTENSITY,
            R.string.error_treatment_intensity_positive, errors
        )
        val totalPulsesValue = nonNegativeInt(
            totalPulses, TreatmentField.TOTAL_PULSES,
            R.string.error_treatment_total_pulses_nonnegative, errors
        )
        val frequencyValue = positiveDouble(
            frequency, TreatmentField.FREQUENCY,
            R.string.error_treatment_frequency_positive, errors
        )
        val motorThresholdValue = positiveDouble(
            motorThreshold, TreatmentField.MOTOR_THRESHOLD,
            R.string.error_treatment_motor_threshold_positive, errors
        )
        val trainsValue = nonNegativeInt(
            trains, TreatmentField.NUMBER_OF_TRAINS,
            R.string.error_treatment_trains_nonnegative, errors
        )
        val pulsesPerTrainValue = nonNegativeInt(
            pulsesPerTrain, TreatmentField.PULSES_PER_TRAIN,
            R.string.error_treatment_pulses_per_train_nonnegative, errors
        )
        val trainDurationValue = positiveDouble(
            trainDuration, TreatmentField.TRAIN_DURATION,
            R.string.error_treatment_train_duration_positive, errors
        )
        val interTrainIntervalValue = positiveDouble(
            interTrainInterval, TreatmentField.INTER_TRAIN_INTERVAL,
            R.string.error_treatment_inter_train_interval_positive, errors
        )
        val sessionDurationValue = positiveDouble(
            sessionDuration, TreatmentField.SESSION_DURATION,
            R.string.error_treatment_session_duration_positive, errors
        )

        if (errors.isNotEmpty()) return TreatmentResult.Invalid(errors)

        return TreatmentResult.Valid(
            TreatmentEntity(
                measurementId = measurementId,
                dateMs = dateMs,
                intensityPctMt = intensityValue,
                totalPulses = totalPulsesValue,
                frequencyHz = frequencyValue,
                motorThresholdPctMso = motorThresholdValue,
                numberOfTrains = trainsValue,
                pulsesPerTrain = pulsesPerTrainValue,
                trainDurationSec = trainDurationValue,
                interTrainIntervalSec = interTrainIntervalValue,
                site = site.trim().ifBlank { null },
                sessionDurationMin = sessionDurationValue,
                notes = notes.trim().ifBlank { null }
            )
        )
    }

    private fun positiveDouble(
        raw: String,
        field: TreatmentField,
        @StringRes messageResId: Int,
        errors: MutableList<TreatmentFieldError>
    ): Double? {
        if (raw.isBlank()) return null
        val parsed = raw.trim().toDoubleOrNull()
        if (parsed == null || parsed <= 0.0) {
            errors.add(TreatmentFieldError(field, messageResId))
            return null
        }
        return parsed
    }

    private fun nonNegativeInt(
        raw: String,
        field: TreatmentField,
        @StringRes messageResId: Int,
        errors: MutableList<TreatmentFieldError>
    ): Int? {
        if (raw.isBlank()) return null
        val parsed = raw.trim().toIntOrNull()
        if (parsed == null || parsed < 0) {
            errors.add(TreatmentFieldError(field, messageResId))
            return null
        }
        return parsed
    }
}

/**
 * Builds an editable [TreatmentDraft] from a persisted [TreatmentEntity] for the edit form.
 * Inverse of [TreatmentDraft.toResult]: null fields become empty strings and Doubles are
 * rendered compactly (whole numbers drop the trailing ".0"), so a value saved as `120.0`
 * shows as "120". The date is carried separately by the form, not by the draft.
 */
fun treatmentDraftFrom(entity: TreatmentEntity): TreatmentDraft = TreatmentDraft(
    intensity = entity.intensityPctMt.toFieldText(),
    totalPulses = entity.totalPulses?.toString().orEmpty(),
    frequency = entity.frequencyHz.toFieldText(),
    motorThreshold = entity.motorThresholdPctMso.toFieldText(),
    trains = entity.numberOfTrains?.toString().orEmpty(),
    pulsesPerTrain = entity.pulsesPerTrain?.toString().orEmpty(),
    trainDuration = entity.trainDurationSec.toFieldText(),
    interTrainInterval = entity.interTrainIntervalSec.toFieldText(),
    site = entity.site.orEmpty(),
    sessionDuration = entity.sessionDurationMin.toFieldText(),
    notes = entity.notes.orEmpty()
)

/** Compact field text for a nullable Double: null/blank when absent, no trailing ".0". */
private fun Double?.toFieldText(): String = when {
    this == null -> ""
    this % 1.0 == 0.0 -> toLong().toString()
    else -> toString()
}
