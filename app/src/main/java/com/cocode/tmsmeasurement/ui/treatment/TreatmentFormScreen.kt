package com.cocode.tmsmeasurement.ui.treatment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cocode.tmsmeasurement.R
import com.cocode.tmsmeasurement.data.local.TreatmentEntity

/**
 * Add or edit a single treatment session for a measurement. Hosts a Material3 date picker
 * (defaults to today, stored as epoch millis), the grouped [TreatmentFormFields], and Save —
 * which validates via [TreatmentDraft.toResult] and persists through [onSave]. In edit mode it
 * prefills from [loadTreatment] and shows a Delete action behind a confirm dialog.
 *
 * Every callback is hoisted so the screen stays free of Room/ViewModel types and unit-friendly.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TreatmentFormScreen(
    innerPadding: PaddingValues,
    measurementId: String,
    editingTreatmentId: Long?,
    nowMs: Long,
    loadTreatment: (Long, (TreatmentEntity?) -> Unit) -> Unit,
    onSave: (TreatmentEntity) -> Unit,
    onDelete: (TreatmentEntity) -> Unit,
    onDone: () -> Unit
) {
    var draft by rememberSaveable(stateSaver = TreatmentDraftSaver) {
        mutableStateOf(TreatmentDraft())
    }
    var dateMs by rememberSaveable { mutableLongStateOf(nowMs) }
    var errors by remember { mutableStateOf<TreatmentErrors>(emptyMap()) }
    var showDelete by remember { mutableStateOf(false) }
    // Prefill the form from the persisted row exactly once; surviving config changes so a
    // rotation mid-edit does not reload over the user's unsaved changes.
    var prefilled by rememberSaveable(editingTreatmentId) { mutableStateOf(editingTreatmentId == null) }

    LaunchedEffect(editingTreatmentId) {
        if (editingTreatmentId != null && !prefilled) {
            loadTreatment(editingTreatmentId) { loaded ->
                if (loaded != null) {
                    draft = treatmentDraftFrom(loaded)
                    dateMs = loaded.dateMs
                }
                prefilled = true
            }
        }
    }

    TreatmentFormBody(
        innerPadding = innerPadding,
        draft = draft,
        onDraftChange = { draft = it },
        errors = errors,
        dateMs = dateMs,
        onDateChange = { dateMs = it },
        isEdit = editingTreatmentId != null,
        onSave = {
            when (val result = draft.toResult(measurementId, dateMs)) {
                is TreatmentResult.Valid -> {
                    errors = emptyMap()
                    // Carry the edited row's id so the upsert updates it; 0 inserts a new row.
                    onSave(result.entity.copy(id = editingTreatmentId ?: 0))
                    onDone()
                }
                is TreatmentResult.Invalid ->
                    errors = result.errors.associate { it.field to it.messageResId }
            }
        },
        onDelete = { showDelete = true }
    )

    if (showDelete) {
        DeleteTreatmentDialog(
            onConfirm = {
                showDelete = false
                // @Delete matches by primary key, so a minimal entity with the id is enough.
                editingTreatmentId?.let { id ->
                    onDelete(TreatmentEntity(id = id, measurementId = measurementId, dateMs = dateMs))
                }
                onDone()
            },
            onDismiss = { showDelete = false }
        )
    }
}

@Composable
private fun TreatmentFormBody(
    innerPadding: PaddingValues,
    draft: TreatmentDraft,
    onDraftChange: (TreatmentDraft) -> Unit,
    errors: TreatmentErrors,
    dateMs: Long,
    onDateChange: (Long) -> Unit,
    isEdit: Boolean,
    onSave: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TreatmentDateField(dateMs = dateMs, onDateChange = onDateChange)
        TreatmentFormFields(draft = draft, onDraftChange = onDraftChange, errors = errors)
        Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.button_save_treatment))
        }
        if (isEdit) {
            OutlinedButton(onClick = onDelete, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.button_delete_treatment))
            }
        }
    }
}
