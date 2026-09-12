# CoastTrip for Android

Native Kotlin / Jetpack Compose client. This folder is the **Gradle root** and the only place Android files belong.

iOS lives in [`../ios/`](../ios/).

## Open in Android Studio

1. **File → Open** and choose this `android/` directory (not the repo root).
2. Copy `local.properties.example` to `local.properties`.
3. Set `sdk.dir` to your Android SDK.
4. Optional: set `MAPS_API_KEY` for Google Maps. Without it, the map tab uses an offline pin canvas and Directions still opens the Google Maps app.

If Android Studio warns that the **Gradle JDK and JAVA_HOME** differ, set both to the same JDK 17–25. The easiest match on a current Mac install is Android Studio’s bundled JBR:

1. **Android Studio → Settings → Build, Execution, Deployment → Build Tools → Gradle**.
2. **Gradle JDK** → **jbr-25** (or **Android Studio java home** / `/Applications/Android Studio.app/Contents/jbr/Contents/Home`).
3. Apply, then **File → Sync Project with Gradle Files**.

The app still compiles to Java 17. Gradle 9.1 can *run* on Java 25, so it can use the same JVM as `JAVA_HOME`.

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
