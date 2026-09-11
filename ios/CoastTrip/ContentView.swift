import SwiftUI
import SwiftData

struct ContentView: View {
    @Query private var trips: [Trip]
    @StateObject private var locationManager = LocationManager()

    var body: some View {
        if let trip = trips.first {
            MainTabView(trip: trip)
                .environmentObject(locationManager)
        } else {
            ProgressView("Loading trip…")
        }
    }
}

struct MainTabView: View {
    let trip: Trip

    @State private var selectedTab = 0
    @State private var showAddStop = false
    @State private var showAddHighlight = false

    var body: some View {
        TabView(selection: $selectedTab) {
            HomeView(trip: trip, onAddStop: { showAddStop = true }, onAddHighlight: { showAddHighlight = true })
                .tabItem {
                    Label("Home", systemImage: "house.fill")
                }
                .tag(0)

            TripMapView(trip: trip, onAddStop: { showAddStop = true }, onAddHighlight: { showAddHighlight = true })
                .tabItem {
                    Label("Map", systemImage: "map.fill")
                }
                .tag(1)

            TimelineView(trip: trip)
                .tabItem {
                    Label("Timeline", systemImage: "clock.fill")
                }
                .tag(2)
        }
        .sheet(isPresented: $showAddStop) {
            AddStopSheet(trip: trip)
        }
        .sheet(isPresented: $showAddHighlight) {
            AddHighlightSheet(trip: trip)
        }
    }
}

#Preview {
    ContentView()
        .modelContainer(for: [Trip.self, Stop.self, Highlight.self, TripPhoto.self], inMemory: true)
}
