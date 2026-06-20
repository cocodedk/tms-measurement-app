package com.cocode.tmsmeasurement.data

import com.cocode.tmsmeasurement.MeasurementRecord
import com.cocode.tmsmeasurement.data.local.MeasurementEntity
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LegacyImporterTest {

    private fun record(id: String, name: String) = MeasurementRecord(
        id = id,
        clientName = name,
        tttCm = 36.0,
        niCm = 38.0,
        hcCm = 60.0,
        xCm = 6.92,
        yCm = 9.76,
        yAdjCm = 10.11,
        timestampMs = 1_725_000_000_000L
    )

    private class FakeSource(records: List<MeasurementRecord>) : LegacySource {
        // Defensive copy so we can detect any mutation attempt against the original.
        private val original = records.toList()
        var loadCount = 0
            private set

        override fun load(): List<MeasurementRecord> {
            loadCount++
            return original
        }

        fun wasMutated(reference: List<MeasurementRecord>): Boolean = original != reference
    }

    private class FakeWriter : MeasurementWriter {
        val stored = LinkedHashMap<String, MeasurementEntity>()
        var failOnInsert = false

        override suspend fun insertAllIfAbsent(items: List<MeasurementEntity>) {
            if (failOnInsert) throw IllegalStateException("insert failed")
            items.forEach { entity -> stored.putIfAbsent(entity.id, entity) }
        }

        override suspend fun count(): Int = stored.size
    }

    private class FakeFlag(initial: Boolean = false) : MigrationFlag {
        private var done = initial
        override fun isDone(): Boolean = done
        override fun markDone() {
            done = true
        }
    }

    @Test
    fun importIfNeeded_importsEveryRecordWithIdenticalFields() = runBlocking {
        val records = listOf(record("a", "Alice"), record("b", "Bob"))
        val source = FakeSource(records)
        val writer = FakeWriter()
        val flag = FakeFlag()

        LegacyImporter(source, writer, flag).importIfNeeded()

        assertEquals(2, writer.stored.size)
        assertEquals(records.map { it.toEntity() }.associateBy { it.id }, writer.stored.toMap())
    }

    @Test
    fun importIfNeeded_runTwice_doesNotDuplicate() = runBlocking {
        val records = listOf(record("a", "Alice"), record("b", "Bob"))
        val source = FakeSource(records)
        val writer = FakeWriter()
        val flag = FakeFlag()
        val importer = LegacyImporter(source, writer, flag)

        importer.importIfNeeded()
        importer.importIfNeeded()

        assertEquals(2, writer.stored.size)
    }

    @Test
    fun importIfNeeded_neverMutatesSourceRecords() = runBlocking {
        val records = listOf(record("a", "Alice"), record("b", "Bob"))
        val source = FakeSource(records)

        LegacyImporter(source, FakeWriter(), FakeFlag()).importIfNeeded()

        assertFalse(source.wasMutated(records))
    }

    @Test
    fun importIfNeeded_setsFlagOnlyAfterSuccess() = runBlocking {
        val source = FakeSource(listOf(record("a", "Alice")))
        val writer = FakeWriter().apply { failOnInsert = true }
        val flag = FakeFlag()

        try {
            LegacyImporter(source, writer, flag).importIfNeeded()
        } catch (_: IllegalStateException) {
            // expected: import failed before the flag could be set
        }

        assertFalse(flag.isDone())
    }

    @Test
    fun importIfNeeded_marksFlagDoneAfterSuccessfulImport() = runBlocking {
        val source = FakeSource(listOf(record("a", "Alice")))
        val flag = FakeFlag()

        LegacyImporter(source, FakeWriter(), flag).importIfNeeded()

        assertTrue(flag.isDone())
    }

    @Test
    fun importIfNeeded_whenFlagAlreadyDone_importsNothingAndNeverReadsSource() = runBlocking {
        val source = FakeSource(listOf(record("a", "Alice")))
        val writer = FakeWriter()
        val flag = FakeFlag(initial = true)

        LegacyImporter(source, writer, flag).importIfNeeded()

        assertEquals(0, writer.stored.size)
        assertEquals(0, source.loadCount)
    }
}
