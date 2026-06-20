package com.cocode.tmsmeasurement.ui.treatment

import com.cocode.tmsmeasurement.data.local.TreatmentEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for [treatmentDraftFrom]: turning a persisted [TreatmentEntity] back into an editable
 * [TreatmentDraft] for the edit form, including the round-trip through [TreatmentDraft.toResult].
 * Pure: no Android types, no org.json.
 */
class TreatmentDraftFromEntityTest {

    private val measurementId = "m-1"
    private val dateMs = 1_725_000_000_000L

    private fun valid(result: TreatmentResult): TreatmentResult.Valid {
        assertTrue("expected Valid but was $result", result is TreatmentResult.Valid)
        return result as TreatmentResult.Valid
    }

    @Test
    fun draftFromEntity_fillsEveryFieldAsCompactStrings() {
        val entity = TreatmentEntity(
            id = 7,
            measurementId = measurementId,
            dateMs = dateMs,
            intensityPctMt = 120.0,
            totalPulses = 3000,
            frequencyHz = 10.0,
            motorThresholdPctMso = 55.5,
            numberOfTrains = 75,
            pulsesPerTrain = 40,
            trainDurationSec = 4.0,
            interTrainIntervalSec = 26.0,
            site = "F3",
            sessionDurationMin = 37.5,
            notes = "first session"
        )

        val draft = treatmentDraftFrom(entity)

        assertEquals("120", draft.intensity)
        assertEquals("3000", draft.totalPulses)
        assertEquals("10", draft.frequency)
        assertEquals("55.5", draft.motorThreshold)
        assertEquals("75", draft.trains)
        assertEquals("40", draft.pulsesPerTrain)
        assertEquals("4", draft.trainDuration)
        assertEquals("26", draft.interTrainInterval)
        assertEquals("F3", draft.site)
        assertEquals("37.5", draft.sessionDuration)
        assertEquals("first session", draft.notes)
    }

    @Test
    fun draftFromEntity_mapsNullFieldsToEmptyStrings() {
        val entity = TreatmentEntity(measurementId = measurementId, dateMs = dateMs)

        val draft = treatmentDraftFrom(entity)

        assertEquals("", draft.intensity)
        assertEquals("", draft.totalPulses)
        assertEquals("", draft.frequency)
        assertEquals("", draft.motorThreshold)
        assertEquals("", draft.trains)
        assertEquals("", draft.pulsesPerTrain)
        assertEquals("", draft.trainDuration)
        assertEquals("", draft.interTrainInterval)
        assertEquals("", draft.site)
        assertEquals("", draft.sessionDuration)
        assertEquals("", draft.notes)
    }

    @Test
    fun draftFromEntity_thenToResult_isLossless() {
        val original = TreatmentEntity(
            id = 9,
            measurementId = measurementId,
            dateMs = dateMs,
            intensityPctMt = 118.0,
            totalPulses = 2400,
            motorThresholdPctMso = 60.0,
            site = "F3",
            notes = "round trip"
        )

        val rebuilt = valid(
            treatmentDraftFrom(original).toResult(measurementId, dateMs)
        ).entity

        assertEquals(original.intensityPctMt, rebuilt.intensityPctMt)
        assertEquals(original.totalPulses, rebuilt.totalPulses)
        assertEquals(original.frequencyHz, rebuilt.frequencyHz)
        assertEquals(original.motorThresholdPctMso, rebuilt.motorThresholdPctMso)
        assertEquals(original.site, rebuilt.site)
        assertEquals(original.notes, rebuilt.notes)
    }
}
