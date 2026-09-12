# CoastTrip for Android

Native Kotlin / Jetpack Compose client. This folder is the **Gradle root** and the only place Android files belong.

iOS lives in [`../ios/`](../ios/).

## Open in Android Studio

1. **File → Open** and choose this `android/` directory (not the repo root).
2. Copy `local.properties.example` to `local.properties`.
3. Set `sdk.dir` to your Android SDK.
4. Optional: set `MAPS_API_KEY` for Google Maps. Without it, the map tab uses an offline pin canvas and Directions still opens the Google Maps app.

If Android Studio reports **Incompatible Gradle JVM version**, the IDE picked Java 25. This project uses Gradle 8.11.1, which only runs on Java 8–23. Point Gradle at **JDK 21** (or 17):

1. **Android Studio → Settings** (or the link on the error banner).
2. **Build, Execution, Deployment → Build Tools → Gradle**.
3. **Gradle JDK** → **21** (Download JDK if it is not listed). Do not leave it on 25.
4. Apply, then **File → Sync Project with Gradle Files**.

The app still compiles to Java 17. Only the JVM that *runs* Gradle needs to change.

A follow-up banner about **multiple Gradle daemons** (`JAVA_HOME` vs Gradle JDK) is a warning, not a failure. Android Studio’s app bundle is often Java 25 (`/Applications/Android Studio.app/Contents/jbr`) while Gradle must stay on 21 (`~/Library/Java/JavaVirtualMachines/jbr-21…`). Keep the Gradle JDK on 21, click **Do not show this warning again**, and continue. Do not point Gradle at the Studio.app `jbr`.

## Command line

```bash
cd android
./gradlew testDebugUnitTest assembleDebug
```

The debug APK is `app/build/outputs/apk/debug/app-debug.apk`.

## What shipped

- Trip list: start, search, edit, delete (no auto-seeded trip)
- Trip workspace: Home, Map, Timeline
- Log / edit / delete stops and highlights
- Photo Picker (no storage permission)
- Just-in-time location rationale
- Miles / kilometers and theme in Settings
- Directions via Google Maps
- Room + DataStore, last opened trip restored after process death

## Layout

```
android/
├── app/                 Compose application
├── docs/                UX spec + HTML prototype
├── build.gradle.kts
└── settings.gradle.kts
```
