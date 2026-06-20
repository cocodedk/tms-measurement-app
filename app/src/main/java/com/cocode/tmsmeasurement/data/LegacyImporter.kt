package com.cocode.tmsmeasurement.data

/**
 * One-time, idempotent import of legacy measurements (SharedPreferences JSON)
 * into Room.
 *
 * Safety guarantees:
 * - Returns early when [flag] is already done, never reading the source again.
 * - Reads the source read-only and never mutates it (the legacy JSON is the
 *   user's irreplaceable data and must survive every run).
 * - Inserts with insert-or-ignore semantics ([MeasurementWriter.insertAllIfAbsent]),
 *   so running more than once cannot duplicate or overwrite rows.
 * - Marks the [flag] done ONLY after a successful insert, so a failure is retried
 *   on the next launch rather than silently skipped.
 */
class LegacyImporter(
    private val source: LegacySource,
    private val writer: MeasurementWriter,
    private val flag: MigrationFlag
) {
    suspend fun importIfNeeded() {
        if (flag.isDone()) return

        val entities = source.load().map { it.toEntity() }
        writer.insertAllIfAbsent(entities)

        flag.markDone()
    }
}
