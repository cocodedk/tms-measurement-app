# Repository Guidelines

## Project Structure & Module Organization
- `app/` is the Android application module.
- Kotlin source lives in `app/src/main/java/com/cocode/tmsmeasurement/`.
- Compose UI theme files are under `app/src/main/java/com/cocode/tmsmeasurement/ui/theme/`.
- Android resources are in `app/src/main/res/` (values, drawables, mipmaps, XML rules).
- Unit tests: `app/src/test/` (local JVM). Instrumentation tests: `app/src/androidTest/` (device/emulator).
- `Docs/` holds product notes such as `Docs/prd.md`.
- Gradle configuration is at the repo root (`build.gradle.kts`, `settings.gradle.kts`, `gradle/libs.versions.toml`).

## Build, Test, and Development Commands
- `./gradlew assembleDebug` builds a debug APK.
- `./gradlew installDebug` installs the debug build on a connected device/emulator.
- `./gradlew test` runs local unit tests.
- `./gradlew connectedAndroidTest` runs instrumentation tests (requires a device/emulator).
- `./gradlew lint` runs Android lint checks.
- `./gradlew clean` removes build outputs.
Use Android Studio for project sync, running, and device management when developing locally.

## Coding Style & Naming Conventions
- Language: Kotlin with Jetpack Compose and Material 3.
- Indentation: 4 spaces; follow Android Studio’s default Kotlin formatter.
- Naming: classes/objects in `PascalCase`, functions/variables in `camelCase`, constants in `UPPER_SNAKE_CASE`.
- Resources use `snake_case` (e.g., `ic_launcher_background.xml`, `themes.xml`).
- Keep new code within the `com.cocode.tmsmeasurement` package structure.

## Testing Guidelines
- Frameworks: JUnit4 for unit tests, AndroidX test runner + Espresso for instrumentation.
- Test class naming: `*Test` matching the target class or feature.
- Put pure logic tests in `app/src/test` and UI/device-dependent tests in `app/src/androidTest`.

## Commit & Pull Request Guidelines
- No Git history is available in this workspace, so no established commit convention was found.
- Suggested commit format: short imperative subject, optional body for rationale (e.g., "Add measurement screen").
- PRs should include: clear description, testing notes (commands run), screenshots for UI changes, and links to relevant issues or `Docs/prd.md`.

## Security & Configuration Tips
- `local.properties` stores local SDK paths; keep it local and uncommitted.
- Avoid hardcoding secrets; prefer `gradle.properties` or environment-based configuration for local-only values.
