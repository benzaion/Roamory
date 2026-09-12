# CoastTrip

Offline road-trip journal. Same product, two native clients. They live in **separate top-level folders** so iOS and Android files never share a tree.

```
ios/          SwiftUI + SwiftData (shipping iOS app)
android/      Material 3 design now; Kotlin / Compose next
```

| Open this | For |
|---|---|
| [`ios/README.md`](ios/README.md) | Xcode project, run on iPhone |
| [`android/README.md`](android/README.md) | Kotlin/Compose app, UX spec, clickable prototype |

Do not add shared source under the repo root. Platform code, resources, and build files stay inside `ios/` or `android/` only.

## Why one repo, two folders

- **One product history.** Issues and PRs can mention both clients without a second remote.
- **Hard isolation.** An Android PR cannot accidentally edit `CoastTrip/*.swift` unless someone crosses the folder on purpose.
- **Independent builds.** Xcode opens `ios/CoastTrip.xcodeproj`. Android Studio opens `android/` as its own Gradle root (`./gradlew assembleDebug`).

Two separate GitHub repositories also work if you later want different permissions or CI. This monorepo is the simpler default while the Android app is still being designed.

## Linux / CI validation

A full build and run requires **Xcode on macOS** — the app is built on Apple-only
frameworks (SwiftUI, SwiftData, MapKit, CoreLocation, PhotosUI, UIKit). It cannot
be compiled on Linux.

For Linux environments (CI, Cloud Agents) the open-source Swift toolchain can still
syntax-check and style-lint the sources:

```sh
scripts/cloud-setup.sh   # install the Swift toolchain (idempotent)
scripts/check-swift.sh   # parse every .swift file + swift-format lint
```

Style rules live in `.swift-format`.

## Privacy

Both clients stay on-device. They request location (when in use), photos, and camera.
