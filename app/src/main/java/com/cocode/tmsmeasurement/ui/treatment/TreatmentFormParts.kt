package com.cocode.tmsmeasurement.ui.treatment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cocode.tmsmeasurement.R
import com.cocode.tmsmeasurement.ui.common.formatDate

/**
 * Session date row: a card showing the chosen date with a button that opens the Material3
 * [DatePickerDialog]. The picker defaults to [dateMs]; confirming reports the new epoch millis.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TreatmentDateField(dateMs: Long, onDateChange: (Long) -> Unit) {
    var showPicker by remember { mutableStateOf(false) }

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.label_treatment_date),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = formatDate(dateMs),
                style = MaterialTheme.typography.bodyLarge
            )
            OutlinedButton(
                onClick = { showPicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.action_pick_date))
            }
        }
    }

    if (showPicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = dateMs)
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let(onDateChange)
                    showPicker = false
                }) {
                    Text(stringResource(R.string.dialog_date_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) {
                    Text(stringResource(R.string.dialog_cancel))
                }
            }
        ) {
            DatePicker(state = state)
        }
    }
}

/** Confirm dialog for deleting a treatment session (irreversible, so it asks first). */
@Composable
internal fun DeleteTreatmentDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_delete_treatment_title)) },
        text = { Text(stringResource(R.string.dialog_delete_treatment_message)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.dialog_delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.dialog_cancel))
            }
        }
    )
}

/**
 * Saves the form's [TreatmentDraft] across configuration changes by flattening its eleven
 * string fields to a list and back. Order here must match [restore] below.
 */
internal val TreatmentDraftSaver: Saver<TreatmentDraft, List<String>> = Saver(
    save = { draft ->
        listOf(
            draft.intensity, draft.totalPulses, draft.frequency, draft.motorThreshold,
            draft.trains, draft.pulsesPerTrain, draft.trainDuration, draft.interTrainInterval,
            draft.site, draft.sessionDuration, draft.notes
        )
    },
    restore = { v ->
        TreatmentDraft(
            intensity = v[0], totalPulses = v[1], frequency = v[2], motorThreshold = v[3],
            trains = v[4], pulsesPerTrain = v[5], trainDuration = v[6], interTrainInterval = v[7],
            site = v[8], sessionDuration = v[9], notes = v[10]
        )
    }
)
