# TMS Measurement App

Open-source Android app for Beam F3 targeting in transcranial magnetic stimulation (TMS), with offline calculations and locally saved measurement and treatment records. Enter three head measurements to calculate X, Y, and adjusted Y distances for F3 site localization, and save them per client for follow-up sessions. Each measurement can also store a log of treatment sessions: intensity, pulses, frequency, motor threshold, pulse-train details, site, and notes.

## Download

<!-- cocode-apps:install:start -->
- Coming to F-Droid
- [Download the Android installation file (APK) from GitHub](https://github.com/cocodedk/tms-measurement-app/releases/latest/download/TMSMeasurement.apk)
- [Add the app to Obtainium, an app that keeps it up to date](https://apps.obtainium.imranr.dev/redirect?r=obtainium://add/https://github.com/cocodedk/tms-measurement-app)
<!-- cocode-apps:install:end -->

## Website

- [English](https://tms.cocode.dk/?lang=en)
- [Dansk](https://tms.cocode.dk/da/)
- [فارسی](https://tms.cocode.dk/?lang=fa)
- [العربية](https://tms.cocode.dk/?lang=ar)
- [繁體中文](https://tms.cocode.dk/?lang=zh-TW)
- [Français](https://tms.cocode.dk/?lang=fr)
- [Español](https://tms.cocode.dk/?lang=es)
- [Deutsch](https://tms.cocode.dk/?lang=de)
- [日本語](https://tms.cocode.dk/?lang=ja)

## Features

- Beam F3 calculations with two-decimal rounding
- Local history of measurement records
- Treatment session logging per measurement: date, intensity as a percentage of motor threshold (MT), total pulses, frequency, motor threshold as a percentage of maximum stimulator output (MSO), pulse-train details, stimulation site and notes
- Validation for required fields
- App languages: English, Dansk, فارسی, العربية and 繁體中文. The website also supports Français, Español, Deutsch and 日本語.

## Privacy

The developer receives none of your data. The app saves your measurements and treatment records on your device and cannot go online: it has no internet permission and requests no runtime permissions. It has no analytics, advertising or cookies.
Android backup or device transfer may copy the saved records, according to your device settings. The About screen and Help button open web pages in your browser only when you tap them. Read the full
[privacy policy](https://tms.cocode.dk/privacy/).

## Build

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

### Release signing

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
│   ├── BeamF3Calculator.kt  # Distance calculations
│   ├── data/                # Room database, repositories
│   ├── viewmodel/           # Application state and persistence coordination
│   └── ui/                  # Jetpack Compose screens
├── docs/         # GitHub Pages site (multilingual)
└── build.gradle.kts
```

| Layer | Tech |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose |
| Database | Room |
| Build | Gradle 9.4.1 |
| Min SDK | 24 (Android 7) |
| Target SDK | 36 |

## Contributing

Local setup, git hooks, the build and test commands, branch naming and the pull request checklist are in
[CONTRIBUTING.md](CONTRIBUTING.md). Bugs and ideas go to the
[issues page](https://github.com/cocodedk/tms-measurement-app/issues).

## Author

**Babak Bandpey** — [cocode.dk](https://cocode.dk) | [LinkedIn](https://linkedin.com/in/babakbandpey) | [GitHub](https://github.com/cocodedk)

## License

MIT, see [LICENSE](LICENSE). &copy; 2026 [Cocode](https://cocode.dk) | Created by [Babak Bandpey](https://linkedin.com/in/babakbandpey)
