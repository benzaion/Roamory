# CoastTrip for Android

Native Kotlin / Jetpack Compose client. This folder is the **Gradle root** and the only place Android files belong.

iOS lives in [`../ios/`](../ios/).

## Open in Android Studio

1. **File → Open** and choose this `android/` directory (not the repo root).
2. Copy `local.properties.example` to `local.properties`.
3. Set `sdk.dir` to your Android SDK.
4. Optional: set `MAPS_API_KEY` for Google Maps. Without it, the map tab uses an offline pin canvas and Directions still opens the Google Maps app.

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
