package com.cocode.tmsmeasurement

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.cocode.tmsmeasurement.ui.MeasurementApp
import com.cocode.tmsmeasurement.ui.theme.TMSMeasurementTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TMSMeasurementTheme {
                MeasurementApp()
            }
        }
    }
}
