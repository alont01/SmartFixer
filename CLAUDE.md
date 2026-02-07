# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

SmartFixer is an Android app ("DIY Home Repair Assistant") built with Kotlin, Jetpack Compose, and Material3. It targets Android API 24-34.

## Build Commands

```bash
./gradlew assembleDebug          # Build debug APK
./gradlew installDebug           # Build and install on connected device
./gradlew test                   # Run unit tests
./gradlew connectedAndroidTest   # Run instrumentation tests (requires device/emulator)
./gradlew clean build            # Clean and full build
```

Run a single unit test class:
```bash
./gradlew test --tests "com.example.smartfixer.ExampleUnitTest"
```

## Architecture

- **Single-module Compose app** — all source under `app/src/main/java/com/example/smartfixer/`
- **MainActivity** — entry point, sets up Scaffold and delegates to `HomeScreen`, defines callback handlers for user actions (diagnose, image/video upload, take photo, view prior fixes, contact pro)
- **HomeScreen** — main UI composable with text input, action buttons, and a bottom dashboard bar using `DashboardButton` composables
- **UI Theme** — Material3 theme in `ui/theme/` with dynamic color support (Android 12+)

## Key Dependencies

- **Retrofit 2** + **Kotlinx Serialization** — set up for REST API calls (not yet wired to a backend)
- **Compose BOM 2024.09.00** — manages all Compose library versions
- Dependency versions are centralized in `gradle/libs.versions.toml`

## Build Configuration

- Kotlin 2.0.21, AGP 8.13.2, Gradle 8.13
- JVM target: 1.8
- Kotlin Compose and Serialization plugins enabled
- ProGuard/minification disabled for release builds
