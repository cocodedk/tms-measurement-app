package com.cocode.tmsmeasurement

import android.content.Context
import org.json.JSONArray

class MeasurementStorage(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun load(): List<MeasurementRecord> {
        val raw = prefs.getString(KEY_RECORDS, null) ?: return emptyList()
        return try {
            val array = JSONArray(raw)
            val records = mutableListOf<MeasurementRecord>()
            for (index in 0 until array.length()) {
                val obj = array.optJSONObject(index) ?: continue
                val record = MeasurementRecord.fromJson(obj) ?: continue
                records.add(record)
            }
            records
        } catch (ex: Exception) {
            emptyList()
        }
    }

    fun saveAll(records: List<MeasurementRecord>) {
        val array = JSONArray()
        records.forEach { array.put(it.toJson()) }
        prefs.edit().putString(KEY_RECORDS, array.toString()).apply()
    }

    companion object {
        private const val PREFS_NAME = "tms_measurements"
        private const val KEY_RECORDS = "records_json"
    }
}
