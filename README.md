# TMS Measurement App

Open-source, offline, privacy-first Android app for Beam F3 targeting in transcranial magnetic stimulation (TMS). Enter three head measurements to calculate X, Y, and adjusted Y distances for F3 site localization, and save them per client for follow-up sessions. Each measurement can also store a log of treatment sessions — intensity, pulses, frequency, motor threshold, train parameters, site, and notes — saved locally per client.

## Download

<!-- cocode-apps:install:start -->
- Coming to F-Droid
- [Download the APK from GitHub](https://github.com/cocodedk/tms-measurement-app/releases/latest/download/TMSMeasurement.apk)
- [Auto-update the GitHub APK with Obtainium](https://apps.obtainium.imranr.dev/redirect?r=obtainium://add/https://github.com/cocodedk/tms-measurement-app)
<!-- cocode-apps:install:end -->

## Website

- [English](https://tms.cocode.dk/)
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
- Treatment session logging per measurement (date, intensity % MT, total pulses, frequency, motor threshold % MSO, train breakdown, site, notes)
- Validation for required fields
- Multilingual support (English, فارسی, العربية, 繁體中文, Français, Español, Deutsch, 日本語)

## Privacy

No data is collected, transmitted, or stored outside the device. The app does not write cookies or track users in any way.
It requests no permissions at all, including no internet access. Read the full
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

## Contributing

Local setup, git hooks, the build and test commands, branch naming and the pull request checklist are in
[CONTRIBUTING.md](CONTRIBUTING.md). Bugs and ideas go to the
[issues page](https://github.com/cocodedk/tms-measurement-app/issues).

## Author

**Babak Bandpey** — [cocode.dk](https://cocode.dk) | [LinkedIn](https://linkedin.com/in/babakbandpey) | [GitHub](https://github.com/cocodedk)

## License

MIT, see [LICENSE](LICENSE). &copy; 2026 [Cocode](https://cocode.dk) | Created by [Babak Bandpey](https://linkedin.com/in/babakbandpey)
