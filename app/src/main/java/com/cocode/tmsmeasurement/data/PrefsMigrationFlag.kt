package com.cocode.tmsmeasurement.data

import android.content.Context

/**
 * SharedPreferences-backed [MigrationFlag].
 *
 * Stores a single boolean marking that the legacy JSON has been imported into
 * Room. Uses its own prefs file — it never touches the legacy
 * "tms_measurements"/"records_json" store, which stays read-only.
 */
class PrefsMigrationFlag(context: Context) : MigrationFlag {
    private val prefs =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun isDone(): Boolean = prefs.getBoolean(KEY_MIGRATED, false)

    override fun markDone() {
        prefs.edit().putBoolean(KEY_MIGRATED, true).commit()
    }

    private companion object {
        const val PREFS_NAME = "tms_migration"
        const val KEY_MIGRATED = "migrated_to_room_v1"
    }
}
