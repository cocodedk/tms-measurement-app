package com.cocode.tmsmeasurement.data

import com.cocode.tmsmeasurement.MeasurementRecord
import com.cocode.tmsmeasurement.MeasurementStorage

/**
 * [LegacySource] backed by [MeasurementStorage] (the legacy SharedPreferences JSON).
 *
 * Read-only by construction: only [MeasurementStorage.load] is ever called, so the
 * user's existing measurements can never be cleared or overwritten by the import.
 */
class StorageLegacySource(private val storage: MeasurementStorage) : LegacySource {
    override fun load(): List<MeasurementRecord> = storage.load()
}
