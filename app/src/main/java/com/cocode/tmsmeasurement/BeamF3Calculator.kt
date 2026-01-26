package com.cocode.tmsmeasurement

import kotlin.math.round

data class BeamF3Result(
    val xCm: Double,
    val yCm: Double,
    val yAdjCm: Double
)

object BeamF3Calculator {
    fun calculate(tttCm: Double, niCm: Double, hcCm: Double): BeamF3Result {
        val avg = (tttCm + niCm) / 2.0
        val rawY = 0.2637 * avg
        return BeamF3Result(
            xCm = roundToTwo(0.1154 * hcCm),
            yCm = roundToTwo(rawY),
            yAdjCm = roundToTwo(rawY + 0.35)
        )
    }

    fun roundToTwo(value: Double): Double = round(value * 100) / 100
}
