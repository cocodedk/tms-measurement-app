package com.cocode.tmsmeasurement.data

import com.cocode.tmsmeasurement.MeasurementRecord

/**
 * Read-only port over the legacy persistence (SharedPreferences JSON).
 *
 * Implementations MUST treat the legacy store as immutable: the importer only
 * ever reads through this interface and never writes back. This guarantees the
 * user's existing measurements are never deleted or overwritten.
 */
interface LegacySource {
    fun load(): List<MeasurementRecord>
}
