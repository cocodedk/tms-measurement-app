# TMS Measurement App

TMS Measurement App is an Android tool that implements the Beam F3 heuristic for
transcranial magnetic stimulation targeting. Enter three head measurements to
calculate X, Y, and adjusted Y distances, and save them per client for follow-up.

## Privacy
No data is collected, transmitted, or stored outside the device. The app does not write cookies or track users in any way.

## Features
- Beam F3 calculations with two-decimal rounding
- Local history of measurement records
- Validation for required fields

## Build
```bash
./gradlew assembleDebug
```

## Tests
```bash
./gradlew test
```

## Release signing
Create `keystore.properties` (ignored by git) for local signed builds:
```
storeFile=keystore/tmsmeasurement-release.jks
storePassword=YOUR_STORE_PASSWORD
keyAlias=tmsmeasurement
keyPassword=YOUR_KEY_PASSWORD
```
For GitHub Actions, set secrets:
`ANDROID_KEYSTORE_BASE64`, `ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS`, `ANDROID_KEY_PASSWORD`.

## About
Made by Babak Bandpey  
bb@cocode.dk  
https://cocode.dk
