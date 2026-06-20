package com.cocode.tmsmeasurement.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cocode.tmsmeasurement.R
import java.text.DateFormat
import java.util.Date

@Composable
fun formatCm(value: Double): String {
    val unit = stringResource(R.string.unit_cm)
    return stringResource(R.string.value_cm_format, value, unit)
}

fun formatTimestamp(timestampMs: Long): String {
    val formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
    return formatter.format(Date(timestampMs))
}

/** Date-only formatter for treatment session dates (no time component). */
fun formatDate(dateMs: Long): String {
    val formatter = DateFormat.getDateInstance(DateFormat.MEDIUM)
    return formatter.format(Date(dateMs))
}
