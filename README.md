# CoastTrip

Offline iOS app for tracking a USA coast-to-coast road trip — locations, photos, and area highlights.

## Requirements

- Xcode 15+ (iOS 17+ target for SwiftData)
- iPhone or Simulator

## Open & Run

1. Open `CoastTrip.xcodeproj` in Xcode
2. Select an iPhone simulator or your device
3. Press **Run** (⌘R)

## Features

- **Home** — trip dashboard with stats (stops, highlights, photos, miles)
- **Map** — route polyline, stop pins, color-coded highlight markers
- **Timeline** — chronological feed grouped by day
- **Add Stop / Highlight** — GPS check-in, notes, photo library or camera
- **Detail views** — edit notes, manage photos, delete entries

All data is stored on-device (SwiftData + local photo files). Works fully offline after install.

## Privacy

The app requests:

- **Location (When In Use)** — to save check-in coordinates
- **Photo Library** — to attach existing photos
- **Camera** — to take photos in the field

## Project Structure

```
CoastTrip/
├── Models/          SwiftData models
├── Views/           SwiftUI screens
├── Services/        Location & photo storage
└── Assets.xcassets
```
