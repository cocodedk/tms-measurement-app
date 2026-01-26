package com.cocode.tmsmeasurement

import org.junit.Assert.assertEquals
import org.junit.Test

class BeamF3CalculatorTest {
    @Test
    fun calculate_returnsExpectedDistances() {
        val result = BeamF3Calculator.calculate(tttCm = 36.0, niCm = 38.0, hcCm = 60.0)

        assertEquals(6.92, result.xCm, 0.0001)
        assertEquals(9.76, result.yCm, 0.0001)
        assertEquals(10.11, result.yAdjCm, 0.0001)
    }
}
