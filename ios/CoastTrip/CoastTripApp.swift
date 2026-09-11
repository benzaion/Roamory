import SwiftUI
import SwiftData

@main
struct CoastTripApp: App {
    let modelContainer: ModelContainer

    init() {
        do {
            modelContainer = try ModelContainer(for: Trip.self, Stop.self, Highlight.self, TripPhoto.self)
        } catch {
            fatalError("Failed to create ModelContainer: \(error)")
        }
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .modelContainer(modelContainer)
                .onAppear {
                    TripSeeder.seedIfNeeded(in: modelContainer.mainContext)
                }
        }
    }
}

enum TripSeeder {
    static func seedIfNeeded(in context: ModelContext) {
        let descriptor = FetchDescriptor<Trip>()
        guard (try? context.fetchCount(descriptor)) == 0 else { return }

        let trip = Trip(
            title: "Coast to Coast 2026",
            startDate: .now,
            notes: "USA coast-to-coast road trip"
        )
        context.insert(trip)
        try? context.save()
    }
}
