# Contributing to tms-measurement-app

## Local Setup
1. Install Android Studio with JDK 17 (Temurin).
2. Clone the repository.
3. Open the project and sync Gradle dependencies.

## Install Git Hooks
```
./scripts/install-hooks.sh
```

## Local Git Setup
```bash
git config pull.rebase true
git config core.autocrlf input
git config push.autoSetupRemote true
git config init.defaultBranch main
```

## Build and Test Commands
```bash
./gradlew assembleDebug           # Build debug APK
./gradlew test                    # Run unit tests
./gradlew lintDebug               # Lint
./gradlew buildSmoke --no-daemon  # Full smoke check (build + tests + lint)
```

## Release Signing (local)
Create `keystore.properties` (ignored by git) for local signed builds:
```
storeFile=keystore/tmsmeasurement-release.jks
storePassword=YOUR_STORE_PASSWORD
keyAlias=tmsmeasurement
keyPassword=YOUR_KEY_PASSWORD
```

For GitHub Actions, set secrets: `ANDROID_KEYSTORE_BASE64`, `ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS`, `ANDROID_KEY_PASSWORD`.

## Branch Naming
| Prefix | Type | Example |
|---|---|---|
| `feature/` | `feat:` | `feature/add-export` |
| `fix/` | `fix:` | `fix/calculation-error` |
| `chore/` | `chore:` | `chore/update-deps` |
| `docs/` | `docs:` | `docs/update-readme` |

## PR Checklist
- [ ] Smoke check passes (`./gradlew buildSmoke --no-daemon`)
- [ ] Manual test completed on device/emulator
- [ ] Updated docs if behavior changed
- [ ] Commit messages follow Conventional Commits
