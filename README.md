# TMS Measurement App

Open-source, offline, privacy-first Android app for Beam F3 targeting in
transcranial magnetic stimulation (TMS). Enter three head measurements to
calculate X, Y, and adjusted Y distances for F3 site localization, and save
them per client for follow-up sessions.

## Website

- [English](https://cocodedk.github.io/tms-measurement-app/)
- [فارسی](https://cocodedk.github.io/tms-measurement-app/?lang=fa)
- [العربية](https://cocodedk.github.io/tms-measurement-app/?lang=ar)
- [繁體中文](https://cocodedk.github.io/tms-measurement-app/?lang=zh-TW)
- [Français](https://cocodedk.github.io/tms-measurement-app/?lang=fr)
- [Español](https://cocodedk.github.io/tms-measurement-app/?lang=es)
- [Deutsch](https://cocodedk.github.io/tms-measurement-app/?lang=de)
- [日本語](https://cocodedk.github.io/tms-measurement-app/?lang=ja)

## Privacy
No data is collected, transmitted, or stored outside the device. The app does not write cookies or track users in any way.

## Features
- Beam F3 calculations with two-decimal rounding
- Local history of measurement records
- Validation for required fields
- Multilingual support (English, فارسی, العربية, 繁體中文, Français, Español, Deutsch, 日本語)

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
