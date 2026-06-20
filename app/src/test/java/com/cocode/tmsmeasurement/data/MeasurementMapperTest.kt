package com.cocode.tmsmeasurement.data

import com.cocode.tmsmeasurement.MeasurementRecord
import org.junit.Assert.assertEquals
import org.junit.Test

class MeasurementMapperTest {

    private fun sampleRecord() = MeasurementRecord(
        id = "abc-123",
        clientName = "Jane Doe",
        tttCm = 36.0,
        niCm = 38.5,
        hcCm = 60.25,
        xCm = 6.92,
        yCm = 9.76,
        yAdjCm = 10.11,
        timestampMs = 1_725_000_000_000L
    )

    @Test
    fun toEntity_copiesEveryFieldExactly() {
        val record = sampleRecord()

        val entity = record.toEntity()

        assertEquals(record.id, entity.id)
        assertEquals(record.clientName, entity.clientName)
        assertEquals(record.tttCm, entity.tttCm, 0.0)
        assertEquals(record.niCm, entity.niCm, 0.0)
        assertEquals(record.hcCm, entity.hcCm, 0.0)
        assertEquals(record.xCm, entity.xCm, 0.0)
        assertEquals(record.yCm, entity.yCm, 0.0)
        assertEquals(record.yAdjCm, entity.yAdjCm, 0.0)
        assertEquals(record.timestampMs, entity.timestampMs)
    }

    @Test
    fun roundTrip_recordToEntityToRecord_isLossless() {
        val original = sampleRecord()

        val restored = original.toEntity().toRecord()

        assertEquals(original, restored)
    }
}
