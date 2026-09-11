# CoastTrip for iOS

Offline SwiftUI app for tracking a USA coast-to-coast road trip — locations, photos, and area highlights.

All iOS source, Xcode project files, and device scripts live in this folder. Android work is in [`../android/`](../android/).

## Requirements

- Xcode 15+ (iOS 17+ target for SwiftData)
- iPhone or Simulator

## Open & Run

1. Open `CoastTrip.xcodeproj` in Xcode (this directory)
2. Select an iPhone simulator or your device
3. Press **Run** (⌘R)

To install on a connected iPhone from the command line:

```bash
./scripts/run-on-iphone.sh
```

## Features

- **Home** — trip dashboard with stats (stops, highlights, photos, miles)
- **Map** — route polyline, stop pins, color-coded highlight markers
- **Timeline** — chronological feed grouped by day
- **Add Stop / Highlight** — GPS check-in, notes, photo library or camera
- **Detail views** — edit notes, manage photos, delete entries

All data is stored on-device (SwiftData + local photo files). Works fully offline after install.

## Project structure

```
ios/
├── CoastTrip/           Swift sources, assets, Info.plist
├── CoastTrip.xcodeproj
└── scripts/             device install helper
```
