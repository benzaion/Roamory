# CoastTrip

Offline road-trip journal. Same product, two native clients. They live in **separate top-level folders** so iOS and Android files never share a tree.

```
ios/          SwiftUI + SwiftData (shipping iOS app)
android/      Material 3 design now; Kotlin / Compose next
```

| Open this | For |
|---|---|
| [`ios/README.md`](ios/README.md) | Xcode project, run on iPhone |
| [`android/README.md`](android/README.md) | Android UX spec, clickable prototype, future Gradle app |

Do not add shared source under the repo root. Platform code, resources, and build files stay inside `ios/` or `android/` only.

## Why one repo, two folders

- **One product history.** Issues and PRs can mention both clients without a second remote.
- **Hard isolation.** An Android PR cannot accidentally edit `CoastTrip/*.swift` unless someone crosses the folder on purpose.
- **Independent builds.** Xcode opens `ios/CoastTrip.xcodeproj`. Android Studio will open `android/` as its own Gradle root.

Two separate GitHub repositories also work if you later want different permissions or CI. This monorepo is the simpler default while the Android app is still being designed.

## Privacy

Both clients stay on-device. They request location (when in use), photos, and camera.
