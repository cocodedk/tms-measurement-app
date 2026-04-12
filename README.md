# TMS Measurement App

Open-source, offline, privacy-first Android app for Beam F3 targeting in transcranial magnetic stimulation (TMS). Enter three head measurements to calculate X, Y, and adjusted Y distances for F3 site localization, and save them per client for follow-up sessions.

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

## Download

[**Download TMS Measurement App**](https://github.com/cocodedk/tms-measurement-app/releases/latest)

## Build from Source

**Prerequisites:** Android Studio, JDK 17 (Temurin)

```bash
git clone https://github.com/cocodedk/tms-measurement-app.git
cd tms-measurement-app
./gradlew assembleDebug
```

**Commands:**
```bash
./gradlew assembleDebug           # Build debug APK
./gradlew test                    # Run unit tests
./gradlew lintDebug               # Lint
./gradlew buildSmoke --no-daemon  # Full smoke check
```

## Release Signing

Create `keystore.properties` (ignored by git) for local signed builds:
```
storeFile=keystore/tmsmeasurement-release.jks
storePassword=YOUR_STORE_PASSWORD
keyAlias=tmsmeasurement
keyPassword=YOUR_KEY_PASSWORD
```

For GitHub Actions, set secrets: `ANDROID_KEYSTORE_BASE64`, `ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS`, `ANDROID_KEY_PASSWORD`.

## Architecture

```
TMSMeasurement/
├── app/src/main/java/com/cocode/tmsmeasurement/
│   ├── data/     # Room database, repositories
│   ├── domain/   # Business logic, use cases
│   └── ui/       # Jetpack Compose screens
├── docs/         # GitHub Pages site (multilingual)
└── build.gradle.kts
```

| Layer | Tech |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose |
| Database | Room |
| Build | Gradle 8 |
| Min SDK | 24 (Android 7) |
| Target SDK | 36 |

## Author

**Babak Bandpey** — [cocode.dk](https://cocode.dk) | [LinkedIn](https://linkedin.com/in/babakbandpey) | [GitHub](https://github.com/cocodedk)

Apache-2.0 | &copy; 2026 [Cocode](https://cocode.dk) | Created by [Babak Bandpey](https://linkedin.com/in/babakbandpey)
