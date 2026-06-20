package com.cocode.tmsmeasurement.data

/**
 * One-time flag marking that the legacy JSON has been imported into Room.
 *
 * Backed in production by SharedPreferences. [markDone] must only be called
 * after a successful import so a failed run is retried on the next launch.
 */
interface MigrationFlag {
    fun isDone(): Boolean
    fun markDone()
}
