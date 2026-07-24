import SwiftUI
import MapKit

struct TripMapView: View {
    let trip: Trip
    let onAddStop: () -> Void
    let onAddHighlight: () -> Void

    @State private var position: MapCameraPosition = .automatic
    @State private var selectedStop: Stop?
    @State private var selectedHighlight: Highlight?

    var body: some View {
        NavigationStack {
            Map(position: $position) {
                if trip.sortedStops.count > 1 {
                    MapPolyline(coordinates: trip.sortedStops.map(\.coordinate))
                        .stroke(.blue, lineWidth: 3)
                }

                ForEach(trip.sortedStops, id: \.id) { stop in
                    Annotation(stop.locationLabel, coordinate: stop.coordinate) {
                        Button {
                            selectedStop = stop
                        } label: {
                            Image(systemName: "mappin.circle.fill")
                                .font(.title2)
                                .foregroundStyle(.blue)
                        }
                    }
                }

                ForEach(trip.highlights, id: \.id) { highlight in
                    Annotation(highlight.name, coordinate: highlight.coordinate) {
                        Image(systemName: highlight.highlightCategory.icon)
                            .padding(6)
                            .background(highlight.highlightCategory.color)
                            .foregroundStyle(.white)
                            .clipShape(Circle())
                            .onTapGesture {
                                selectedHighlight = highlight
                            }
                    }
                }
            }
            .mapControls {
                MapUserLocationButton()
                MapCompass()
            }
            .navigationTitle("Map")
            .sheet(item: $selectedStop) { stop in
                NavigationStack {
                    StopDetailView(stop: stop)
                        .toolbar {
                            ToolbarItem(placement: .confirmationAction) {
                                Button("Done") { selectedStop = nil }
                            }
                        }
                }
            }
            .sheet(item: $selectedHighlight) { highlight in
                NavigationStack {
                    HighlightDetailView(highlight: highlight)
                        .toolbar {
                            ToolbarItem(placement: .confirmationAction) {
                                Button("Done") { selectedHighlight = nil }
                            }
                        }
                }
            }
            .toolbar {
                ToolbarItemGroup(placement: .bottomBar) {
                    Button(action: onAddStop) {
                        Label("Stop", systemImage: "mappin.circle.fill")
                    }
                    Spacer()
                    Button(action: onAddHighlight) {
                        Label("Highlight", systemImage: "star.circle.fill")
                    }
                }
            }
            .onAppear {
                fitMapToTrip()
            }
            .onChange(of: trip.stops.count) { _, _ in fitMapToTrip() }
            .onChange(of: trip.highlights.count) { _, _ in fitMapToTrip() }
        }
    }

    private func fitMapToTrip() {
        let coordinates = trip.sortedStops.map(\.coordinate) + trip.highlights.map(\.coordinate)
        guard !coordinates.isEmpty else {
            position = .region(MKCoordinateRegion(
                center: CLLocationCoordinate2D(latitude: 39.8283, longitude: -98.5795),
                span: MKCoordinateSpan(latitudeDelta: 40, longitudeDelta: 50)
            ))
            return
        }

        if coordinates.count == 1 {
            position = .region(MKCoordinateRegion(
                center: coordinates[0],
                span: MKCoordinateSpan(latitudeDelta: 0.5, longitudeDelta: 0.5)
            ))
            return
        }

        var minLat = coordinates[0].latitude
        var maxLat = coordinates[0].latitude
        var minLon = coordinates[0].longitude
        var maxLon = coordinates[0].longitude

        for coordinate in coordinates {
            minLat = min(minLat, coordinate.latitude)
            maxLat = max(maxLat, coordinate.latitude)
            minLon = min(minLon, coordinate.longitude)
            maxLon = max(maxLon, coordinate.longitude)
        }

        let center = CLLocationCoordinate2D(
            latitude: (minLat + maxLat) / 2,
            longitude: (minLon + maxLon) / 2
        )
        let span = MKCoordinateSpan(
            latitudeDelta: max(0.5, (maxLat - minLat) * 1.4),
            longitudeDelta: max(0.5, (maxLon - minLon) * 1.4)
        )
        position = .region(MKCoordinateRegion(center: center, span: span))
    }
}
