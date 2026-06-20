package com.cocode.tmsmeasurement.ui.measurement

import com.cocode.tmsmeasurement.BeamF3Calculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Locks in the exact behavior of the calculate-and-save logic that was extracted
 * out of MeasurementApp's inline onCalculate lambda during the Phase 4a split.
 * Plain JVM only: constructs objects directly, never touches org.json.
 */
class CalculateMeasurementTest {

    private val errors = MeasurementErrorMessages(
        clientRequired = "client",
        tttPositive = "ttt",
        niPositive = "ni",
        hcPositive = "hc"
    )

    private fun calc(
        name: String = "Alice",
        ttt: String = "20",
        ni: String = "18",
        hc: String = "56",
        now: Long = 1000L
    ) = calculateMeasurement(name, ttt, ni, hc, errors, now)

    @Test
    fun `blank client name yields client-required error`() {
        val outcome = calc(name = "   ")
        assertEquals(MeasurementCalcOutcome.Error(errors.clientRequired), outcome)
    }

    @Test
    fun `non-numeric ttt yields ttt error`() {
        val outcome = calc(ttt = "abc")
        assertEquals(MeasurementCalcOutcome.Error(errors.tttPositive), outcome)
    }

    @Test
    fun `zero ni yields ni error`() {
        val outcome = calc(ni = "0")
        assertEquals(MeasurementCalcOutcome.Error(errors.niPositive), outcome)
    }

    @Test
    fun `negative hc yields hc error`() {
        val outcome = calc(hc = "-3")
        assertEquals(MeasurementCalcOutcome.Error(errors.hcPositive), outcome)
    }

    @Test
    fun `valid inputs produce a success record with calculator values`() {
        val outcome = calc(ttt = "20", ni = "18", hc = "56", now = 1234L)
        assertTrue(outcome is MeasurementCalcOutcome.Success)
        val record = (outcome as MeasurementCalcOutcome.Success).record
        val expected = BeamF3Calculator.calculate(20.0, 18.0, 56.0)
        assertEquals(expected.xCm, record.xCm, 0.0)
        assertEquals(expected.yCm, record.yCm, 0.0)
        assertEquals(expected.yAdjCm, record.yAdjCm, 0.0)
    }

    @Test
    fun `success record trims name and uses now for id and timestamp`() {
        val outcome = calc(name = "  Bob  ", now = 777L)
        val record = (outcome as MeasurementCalcOutcome.Success).record
        assertEquals("Bob", record.clientName)
        assertEquals("777", record.id)
        assertEquals(777L, record.timestampMs)
    }

    @Test
    fun `success record rounds raw inputs to two decimals`() {
        val outcome = calc(ttt = "20.005", ni = "18.0", hc = "56.0")
        val record = (outcome as MeasurementCalcOutcome.Success).record
        assertEquals(BeamF3Calculator.roundToTwo(20.005), record.tttCm, 0.0)
    }
}
