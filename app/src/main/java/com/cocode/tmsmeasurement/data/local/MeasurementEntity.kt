package com.cocode.tmsmeasurement.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "measurements")
data class MeasurementEntity(
    @PrimaryKey val id: String,
    val clientName: String,
    val tttCm: Double,
    val niCm: Double,
    val hcCm: Double,
    val xCm: Double,
    val yCm: Double,
    val yAdjCm: Double,
    val timestampMs: Long
)
