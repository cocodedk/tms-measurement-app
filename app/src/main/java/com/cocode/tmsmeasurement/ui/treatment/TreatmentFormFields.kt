package com.cocode.tmsmeasurement.ui.treatment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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

/** Per-field validation messages keyed by [TreatmentField], built from a [TreatmentResult.Invalid]. */
typealias TreatmentErrors = Map<TreatmentField, Int>

/**
 * The treatment form's grouped input sections, mirroring the measurement [InputSection] style:
 * each value is an [OutlinedTextField] with a decimal keyboard and a unit suffix, and shows its
 * own validation error underneath. State is hoisted: the parent owns the [draft] and applies the
 * lambda each field returns via [onDraftChange]. Pure UI — no persistence or parsing here.
 */
@Composable
internal fun TreatmentFormFields(
    draft: TreatmentDraft,
    onDraftChange: (TreatmentDraft) -> Unit,
    errors: TreatmentErrors
) {
    FormSection(R.string.section_treatment_essentials) {
        NumberField(
            value = draft.intensity, label = R.string.label_treatment_intensity,
            unit = R.string.unit_percent_mt, field = TreatmentField.INTENSITY, errors = errors,
            onValueChange = { onDraftChange(draft.copy(intensity = it)) }
        )
        NumberField(
            value = draft.totalPulses, label = R.string.label_treatment_total_pulses,
            unit = null, field = TreatmentField.TOTAL_PULSES, errors = errors,
            onValueChange = { onDraftChange(draft.copy(totalPulses = it)) }
        )
        NumberField(
            value = draft.frequency, label = R.string.label_treatment_frequency,
            unit = R.string.unit_hz, field = TreatmentField.FREQUENCY, errors = errors,
            onValueChange = { onDraftChange(draft.copy(frequency = it)) }
        )
    }

    FormSection(R.string.section_treatment_motor_threshold) {
        NumberField(
            value = draft.motorThreshold, label = R.string.label_treatment_motor_threshold,
            unit = R.string.unit_percent_mso, field = TreatmentField.MOTOR_THRESHOLD, errors = errors,
            onValueChange = { onDraftChange(draft.copy(motorThreshold = it)) }
        )
    }

    FormSection(R.string.section_treatment_train) {
        NumberField(
            value = draft.trains, label = R.string.label_treatment_trains,
            unit = null, field = TreatmentField.NUMBER_OF_TRAINS, errors = errors,
            onValueChange = { onDraftChange(draft.copy(trains = it)) }
        )
        NumberField(
            value = draft.pulsesPerTrain, label = R.string.label_treatment_pulses_per_train,
            unit = null, field = TreatmentField.PULSES_PER_TRAIN, errors = errors,
            onValueChange = { onDraftChange(draft.copy(pulsesPerTrain = it)) }
        )
        NumberField(
            value = draft.trainDuration, label = R.string.label_treatment_train_duration,
            unit = R.string.unit_seconds, field = TreatmentField.TRAIN_DURATION, errors = errors,
            onValueChange = { onDraftChange(draft.copy(trainDuration = it)) }
        )
        NumberField(
            value = draft.interTrainInterval, label = R.string.label_treatment_inter_train_interval,
            unit = R.string.unit_seconds, field = TreatmentField.INTER_TRAIN_INTERVAL, errors = errors,
            onValueChange = { onDraftChange(draft.copy(interTrainInterval = it)) }
        )
    }

    FormSection(R.string.section_treatment_context) {
        OutlinedTextField(
            value = draft.site,
            onValueChange = { onDraftChange(draft.copy(site = it)) },
            label = { Text(stringResource(R.string.label_treatment_site)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        NumberField(
            value = draft.sessionDuration, label = R.string.label_treatment_session_duration,
            unit = R.string.unit_minutes, field = TreatmentField.SESSION_DURATION, errors = errors,
            onValueChange = { onDraftChange(draft.copy(sessionDuration = it)) }
        )
        OutlinedTextField(
            value = draft.notes,
            onValueChange = { onDraftChange(draft.copy(notes = it)) },
            label = { Text(stringResource(R.string.label_treatment_notes)) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/** A titled [Card] grouping related fields, matching the measurement screen's card style. */
@Composable
private fun FormSection(titleResId: Int, content: @Composable () -> Unit) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(titleResId),
                style = MaterialTheme.typography.titleMedium
            )
            content()
        }
    }
}

/** A numeric [OutlinedTextField] with a decimal keyboard, optional unit suffix and inline error. */
@Composable
private fun NumberField(
    value: String,
    label: Int,
    unit: Int?,
    field: TreatmentField,
    errors: TreatmentErrors,
    onValueChange: (String) -> Unit
) {
    val errorResId = errors[field]
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = errorResId != null,
        suffix = unit?.let { { Text(stringResource(it)) } },
        supportingText = errorResId?.let { { Text(stringResource(it)) } },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}
