package com.cocode.tmsmeasurement.ui.treatment

import com.cocode.tmsmeasurement.R
import com.cocode.tmsmeasurement.data.local.TreatmentEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pure validation tests for [TreatmentDraft]. No Android types, no org.json:
 * [TreatmentDraft.toResult] only parses raw strings into a [TreatmentEntity]
 * or a list of field errors that reference string-resource id constants.
 */
class TreatmentDraftTest {

    private val measurementId = "m-1"
    private val dateMs = 1_725_000_000_000L

    private fun valid(result: TreatmentResult): TreatmentResult.Valid {
        assertTrue("expected Valid but was $result", result is TreatmentResult.Valid)
        return result as TreatmentResult.Valid
    }

    private fun invalid(result: TreatmentResult): TreatmentResult.Invalid {
        assertTrue("expected Invalid but was $result", result is TreatmentResult.Invalid)
        return result as TreatmentResult.Invalid
    }

    @Test
    fun fullValidInput_parsesEveryFieldIntoEntity() {
        val draft = TreatmentDraft(
            intensity = "120",
            totalPulses = "3000",
            frequency = "10",
            motorThreshold = "55.5",
            trains = "75",
            pulsesPerTrain = "40",
            trainDuration = "4",
            interTrainInterval = "26",
            site = "  F3  ",
            sessionDuration = "37.5",
            notes = "  first session  "
        )

        val entity = valid(draft.toResult(measurementId, dateMs)).entity

        assertEquals(measurementId, entity.measurementId)
        assertEquals(dateMs, entity.dateMs)
        assertEquals(120.0, entity.intensityPctMt)
        assertEquals(3000, entity.totalPulses)
        assertEquals(10.0, entity.frequencyHz)
        assertEquals(55.5, entity.motorThresholdPctMso)
        assertEquals(75, entity.numberOfTrains)
        assertEquals(40, entity.pulsesPerTrain)
        assertEquals(4.0, entity.trainDurationSec)
        assertEquals(26.0, entity.interTrainIntervalSec)
        assertEquals("F3", entity.site)
        assertEquals(37.5, entity.sessionDurationMin)
        assertEquals("first session", entity.notes)
    }

    @Test
    fun minimalValidInput_onlyDate_leavesAllClinicalFieldsNull() {
        val draft = TreatmentDraft()

        val entity = valid(draft.toResult(measurementId, dateMs)).entity

        assertEquals(measurementId, entity.measurementId)
        assertEquals(dateMs, entity.dateMs)
        assertNull(entity.intensityPctMt)
        assertNull(entity.totalPulses)
        assertNull(entity.frequencyHz)
        assertNull(entity.motorThresholdPctMso)
        assertNull(entity.numberOfTrains)
        assertNull(entity.pulsesPerTrain)
        assertNull(entity.trainDurationSec)
        assertNull(entity.interTrainIntervalSec)
        assertNull(entity.site)
        assertNull(entity.sessionDurationMin)
        assertNull(entity.notes)
    }

    @Test
    fun blankOptionalText_mapsToNull() {
        val draft = TreatmentDraft(site = "   ", notes = "")

        val entity = valid(draft.toResult(measurementId, dateMs)).entity

        assertNull(entity.site)
        assertNull(entity.notes)
    }

    @Test
    fun nonNumericDoubleField_reportsParseError() {
        val draft = TreatmentDraft(intensity = "abc")

        val errors = invalid(draft.toResult(measurementId, dateMs)).errors

        assertEquals(1, errors.size)
        assertEquals(TreatmentField.INTENSITY, errors.first().field)
        assertEquals(R.string.error_treatment_intensity_positive, errors.first().messageResId)
    }

    @Test
    fun nonNumericIntField_reportsParseError() {
        val draft = TreatmentDraft(totalPulses = "12.5")

        val errors = invalid(draft.toResult(measurementId, dateMs)).errors

        assertEquals(1, errors.size)
        assertEquals(TreatmentField.TOTAL_PULSES, errors.first().field)
        assertEquals(R.string.error_treatment_total_pulses_nonnegative, errors.first().messageResId)
    }

    @Test
    fun zeroDoubleField_isRejected() {
        val draft = TreatmentDraft(frequency = "0")

        val errors = invalid(draft.toResult(measurementId, dateMs)).errors

        assertEquals(1, errors.size)
        assertEquals(TreatmentField.FREQUENCY, errors.first().field)
    }

    @Test
    fun negativeDoubleField_isRejected() {
        val draft = TreatmentDraft(sessionDuration = "-5")

        val errors = invalid(draft.toResult(measurementId, dateMs)).errors

        assertEquals(1, errors.size)
        assertEquals(TreatmentField.SESSION_DURATION, errors.first().field)
    }

    @Test
    fun negativeIntField_isRejected() {
        val draft = TreatmentDraft(trains = "-1")

        val errors = invalid(draft.toResult(measurementId, dateMs)).errors

        assertEquals(1, errors.size)
        assertEquals(TreatmentField.NUMBER_OF_TRAINS, errors.first().field)
    }

    @Test
    fun zeroIntField_isAcceptedAsNonNegative() {
        val draft = TreatmentDraft(totalPulses = "0")

        val entity = valid(draft.toResult(measurementId, dateMs)).entity

        assertEquals(0, entity.totalPulses)
    }

    @Test
    fun multipleInvalidFields_areAllReported() {
        val draft = TreatmentDraft(
            intensity = "0",
            totalPulses = "bad",
            frequency = "-3"
        )

        val errors = invalid(draft.toResult(measurementId, dateMs)).errors

        assertEquals(3, errors.size)
        val fields = errors.map { it.field }.toSet()
        assertTrue(fields.contains(TreatmentField.INTENSITY))
        assertTrue(fields.contains(TreatmentField.TOTAL_PULSES))
        assertTrue(fields.contains(TreatmentField.FREQUENCY))
    }
}
