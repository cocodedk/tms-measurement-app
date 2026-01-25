# TMS Measurement

TMS Measurement is an Android app that calculates F3 positioning for transcranial magnetic stimulation (TMS) using the Beam F3 heuristic. It computes coordinates from three simple head measurements (tragus-to-tragus, nasion-to-inion, and head circumference) to help clinicians locate the F3 position (left dorsolateral prefrontal cortex) without expensive neuronavigation systems.

## Features

- **Beam F3 calculation** - Implements the validated Beam F3 method for TMS targeting
- **Client records** - Save measurements with client names for clinical documentation
- **Measurement history** - View and manage past measurements with timestamps
- **Offline-first** - All data stored locally, no network required
- **Simple interface** - Clean, clinician-friendly UI built with Jetpack Compose
- **Accurate calculations** - Uses published heuristics with MRI-guided adjustments

## Calculation Method

The app uses three head measurements to calculate F3 coordinates:

1. **Tragus-to-tragus (TTT)** - Distance between left and right tragus
2. **Nasion-to-inion (NI)** - Distance from nasion to inion
3. **Head circumference (HC)** - Circumference measured through FPz–Oz

From these inputs, the app calculates:
- **X (circumferential distance)** - 11.54% of head circumference from midline
- **Y (radial distance)** - 26.37% of average TTT and NI distances
- **Adjusted Y** - Y + 0.35 cm correction for MRI-guided alignment

## Build

```bash
./gradlew assembleDebug
```

## Scientific Basis

The Beam F3 method is based on research showing that geometric parameters accurately approximate the F3 EEG position when expressed as percentages of standard head measurements. The calculation method is consistent with updated scalp heuristics recommended by Mir-Moghtadaei and colleagues (2015) and web implementations of the Beam F3 program.

## Privacy

- All data is stored locally on the device
- No network access required
- No data collection or analytics
- Client records remain private and secure

## About

Made by Babak Bandpey  
bb@cocode.dk  
https://cocode.dk
