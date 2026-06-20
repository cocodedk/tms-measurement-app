package com.cocode.tmsmeasurement.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "treatments",
    foreignKeys = [
        ForeignKey(
            entity = MeasurementEntity::class,
            parentColumns = ["id"],
            childColumns = ["measurementId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TreatmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(index = true) val measurementId: String,
    val dateMs: Long,
    val intensityPctMt: Double? = null,
    val totalPulses: Int? = null,
    val frequencyHz: Double? = null,
    val motorThresholdPctMso: Double? = null,
    val numberOfTrains: Int? = null,
    val pulsesPerTrain: Int? = null,
    val trainDurationSec: Double? = null,
    val interTrainIntervalSec: Double? = null,
    val site: String? = null,
    val sessionDurationMin: Double? = null,
    val notes: String? = null
)
