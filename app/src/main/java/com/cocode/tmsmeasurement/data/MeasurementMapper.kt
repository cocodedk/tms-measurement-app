package com.cocode.tmsmeasurement.data

import com.cocode.tmsmeasurement.MeasurementRecord
import com.cocode.tmsmeasurement.data.local.MeasurementEntity

/**
 * Lossless conversion between the legacy [MeasurementRecord] (JSON-backed, import-only)
 * and the Room [MeasurementEntity]. Every field maps one-to-one, so a round trip
 * (record -> entity -> record) preserves all data.
 */
fun MeasurementRecord.toEntity(): MeasurementEntity = MeasurementEntity(
    id = id,
    clientName = clientName,
    tttCm = tttCm,
    niCm = niCm,
    hcCm = hcCm,
    xCm = xCm,
    yCm = yCm,
    yAdjCm = yAdjCm,
    timestampMs = timestampMs
)

fun MeasurementEntity.toRecord(): MeasurementRecord = MeasurementRecord(
    id = id,
    clientName = clientName,
    tttCm = tttCm,
    niCm = niCm,
    hcCm = hcCm,
    xCm = xCm,
    yCm = yCm,
    yAdjCm = yAdjCm,
    timestampMs = timestampMs
)
