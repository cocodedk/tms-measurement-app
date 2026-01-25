package com.cocode.tmsmeasurement

import org.json.JSONObject

data class MeasurementRecord(
    val id: String,
    val clientName: String,
    val tttCm: Double,
    val niCm: Double,
    val hcCm: Double,
    val xCm: Double,
    val yCm: Double,
    val yAdjCm: Double,
    val timestampMs: Long
) {
    fun toJson(): JSONObject {
        val obj = JSONObject()
        obj.put("id", id)
        obj.put("clientName", clientName)
        obj.put("tttCm", tttCm)
        obj.put("niCm", niCm)
        obj.put("hcCm", hcCm)
        obj.put("xCm", xCm)
        obj.put("yCm", yCm)
        obj.put("yAdjCm", yAdjCm)
        obj.put("timestampMs", timestampMs)
        return obj
    }

    companion object {
        fun fromJson(obj: JSONObject): MeasurementRecord? {
            return try {
                MeasurementRecord(
                    id = obj.getString("id"),
                    clientName = obj.getString("clientName"),
                    tttCm = obj.getDouble("tttCm"),
                    niCm = obj.getDouble("niCm"),
                    hcCm = obj.getDouble("hcCm"),
                    xCm = obj.getDouble("xCm"),
                    yCm = obj.getDouble("yCm"),
                    yAdjCm = obj.getDouble("yAdjCm"),
                    timestampMs = obj.getLong("timestampMs")
                )
            } catch (ex: Exception) {
                null
            }
        }
    }
}
