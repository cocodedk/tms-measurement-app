package com.cocode.tmsmeasurement.ui.detail

import com.cocode.tmsmeasurement.data.local.TreatmentEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pure tests for [treatmentSummaryChips]. No Android types, no org.json: the
 * helper only selects which clinical values are present on a [TreatmentEntity]
 * and returns them in a fixed priority order as plain number strings, so the
 * Composable can wrap each in a localized [androidx.compose.ui.res.stringResource].
 */
class TreatmentSummaryTest {

    private fun entity(
        intensity: Double? = null,
        totalPulses: Int? = null,
        frequency: Double? = null,
        motorThreshold: Double? = null
    ) = TreatmentEntity(
        measurementId = "m-1",
        dateMs = 1_725_000_000_000L,
        intensityPctMt = intensity,
        totalPulses = totalPulses,
        frequencyHz = frequency,
        motorThresholdPctMso = motorThreshold
    )

    @Test
    fun emptyTreatment_hasNoChips() {
        assertTrue(treatmentSummaryChips(entity()).isEmpty())
    }

    @Test
    fun presentValues_appearInPriorityOrder() {
        val chips = treatmentSummaryChips(
            entity(intensity = 120.0, totalPulses = 3000, frequency = 10.0)
        )
        assertEquals(
            listOf(
                TreatmentChipType.INTENSITY,
                TreatmentChipType.TOTAL_PULSES,
                TreatmentChipType.FREQUENCY
            ),
            chips.map { it.type }
        )
    }

    @Test
    fun doubleChip_dropsTrailingZeroDecimal() {
        val chips = treatmentSummaryChips(entity(intensity = 120.0))
        assertEquals("120", chips.single().value)
    }

    @Test
    fun doubleChip_keepsMeaningfulDecimal() {
        val chips = treatmentSummaryChips(entity(motorThreshold = 55.5))
        assertEquals("55.5", chips.single().value)
    }

    @Test
    fun intChip_rendersWithoutDecimal() {
        val chips = treatmentSummaryChips(entity(totalPulses = 3000))
        assertEquals("3000", chips.single().value)
    }

    @Test
    fun chipCount_isCappedAtMax() {
        val chips = treatmentSummaryChips(
            entity(intensity = 1.0, totalPulses = 2, frequency = 3.0, motorThreshold = 4.0),
            maxChips = 2
        )
        assertEquals(2, chips.size)
    }
}
