# CLAUDE.md — tms-measurement-app

## Project Overview

Open-source, offline, privacy-first Android app for Beam F3 targeting in transcranial magnetic stimulation (TMS). Enter three head measurements to calculate X, Y, and adjusted Y distances for F3 site localization, and save them per client for follow-up sessions.

- **Language / Runtime**: Kotlin 2.x, JDK 17
- **Framework**: Jetpack Compose, Android SDK 36
- **Architecture**: Clean Architecture with MVVM, Jetpack Compose UI
- **Package / Namespace**: `com.cocode.tmsmeasurement`

---

## Required Skills — ALWAYS Invoke These

These skills **must** be invoked when the relevant situation arises. Never skip them.

| Situation | Skill |
|-----------|-------|
| Before any new feature or screen | `superpowers:brainstorming` |
| Planning multi-step changes | `superpowers:writing-plans` |
| Writing or fixing core logic | `superpowers:test-driven-development` |
| First sign of a bug or failure | `superpowers:systematic-debugging` |
| Before completing a feature branch | `superpowers:requesting-code-review` |
| Before claiming any task done | `superpowers:verification-before-completion` |
| Working on UI / frontend | `frontend-design:frontend-design` |
| After implementing — reviewing quality | `simplify` |

---

## Architecture

```
TMSMeasurement/
├── app/
│   └── src/
│       ├── main/
│       │   ├── java/com/cocode/tmsmeasurement/
│       │   │   ├── data/          <- Room database, repositories
│       │   │   ├── domain/        <- Business logic, use cases
│       │   │   ├── ui/            <- Jetpack Compose screens
│       │   │   └── MainActivity.kt
│       │   └── res/               <- Resources
│       └── test/                  <- Unit tests
├── docs/                          <- GitHub Pages site (multilingual)
└── build.gradle.kts               <- Root build file
```

### Layer Rules
- `ui/` must not contain business logic — delegate to ViewModel or use cases
- `data/` must not be accessed directly from `ui/` — go through repository
- No network access — fully offline app

---

## Coding Conventions

- All models are **immutable** — use `copy()` for mutations
- Functions are **pure** where possible — no hidden side effects
- No hardcoded strings — use string resources
- Strict Kotlin — no `!!` null assertions except in tests

---

## Engineering Principles

### File Size
- **200-line maximum per file** — extract a composable, ViewModel, or class when approaching the limit

### DRY · SOLID · KISS · YAGNI
- Extract shared logic into named utilities; never copy-paste
- Single Responsibility: one class/composable does one thing
- Don't add features not yet needed
- Delete dead code immediately

### TDD
- Write the failing test first, make it pass, then refactor
- Test names describe behaviour: `"should calculate F3 correctly for given measurements"`
- One assertion per test — keep tests focused and readable

### Commit hygiene
- Follow Conventional Commits: `feat: ...` / `fix: ...` / `chore: ...`
- The `commit-msg` hook enforces this automatically

---

## Build Commands

```bash
./gradlew assembleDebug           # Build debug APK
./gradlew assembleRelease         # Build release APK (requires keystore)
./gradlew test                    # Run unit tests
./gradlew lintDebug               # Lint
./gradlew buildSmoke --no-daemon  # Full smoke check — used in CI and pre-commit
```

---

## Key Files

| File | Purpose |
|------|---------|
| `CLAUDE.md` | This file — project conventions and session startup |
| `app/build.gradle.kts` | App build config with version code/name |
| `.github/workflows/` | CI, release APK, Pages automation |
| `.githooks/` | Pre-commit and commit-msg hooks |
| `scripts/install-hooks.sh` | One-time hook installer |
| `docs/` | GitHub Pages multilingual site |

---

## Starting a New Session

1. Read this file
2. Run `./gradlew buildSmoke --no-daemon` to confirm everything passes
3. Invoke `superpowers:brainstorming` before touching any feature
4. Follow the Required Skills table — every skill is mandatory, not optional
