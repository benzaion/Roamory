# CoastTrip for Android

Native Android client. This folder is the **only** place Android files belong.

iOS lives in [`../ios/`](../ios/). Do not put Gradle, Kotlin, or Android resources next to the Xcode project.

## Now

UX spec and a clickable Material 3 prototype (no app binary yet):

- [`docs/DESIGN.md`](docs/DESIGN.md) — product and UX source of truth
- [`docs/prototype/index.html`](docs/prototype/index.html) — phone-framed flows

Open the HTML file in a browser.

Locked decisions: many trips, Google Maps, miles and kilometers, Directions via Google Maps, Android extras (search, chips, settings, captions).

## Next

The Kotlin / Compose project will be created **in this folder** as a standard Gradle root:

```
android/
├── docs/                design + prototype (already here)
├── app/                 future application module
├── build.gradle.kts
└── settings.gradle.kts
```

Android Studio: **Open** the `android/` directory, not the repo root.
