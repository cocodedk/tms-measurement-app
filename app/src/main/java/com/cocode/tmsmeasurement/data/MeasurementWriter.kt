package com.cocode.tmsmeasurement.data

import com.cocode.tmsmeasurement.data.local.MeasurementEntity

/**
 * Write port for importing measurements into Room.
 *
 * [insertAllIfAbsent] must be insert-or-ignore on the primary key so repeated
 * imports never duplicate or overwrite rows. Signatures mirror
 * [com.cocode.tmsmeasurement.data.local.MeasurementDao] so the DAO satisfies
 * this contract directly. All access is suspending (coroutines on Dispatchers.IO).
 */
interface MeasurementWriter {
    suspend fun insertAllIfAbsent(items: List<MeasurementEntity>)
    suspend fun count(): Int
}
