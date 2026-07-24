import SwiftUI
import MapKit
import CoreLocation

struct LocationCaptureView: View {
    @EnvironmentObject private var locationManager: LocationManager

    @Binding var latitude: Double?
    @Binding var longitude: Double?
    @Binding var placeName: String

    @State private var mapPosition: MapCameraPosition = .automatic

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            if locationManager.isAuthorized, let location = locationManager.currentLocation {
                Label(
                    String(format: "%.4f, %.4f", location.coordinate.latitude, location.coordinate.longitude),
                    systemImage: "location.fill"
                )
                .font(.subheadline)
                .foregroundStyle(.green)

                Button("Use Current Location") {
                    setCoordinate(location.coordinate)
                    if placeName.isEmpty {
                        placeName = "Current Location"
                    }
                }
                .buttonStyle(.borderedProminent)
            } else if locationManager.authorizationStatus == .denied || locationManager.authorizationStatus == .restricted {
                Label("Location access denied. Tap the map to pick a spot.", systemImage: "location.slash")
                    .font(.subheadline)
                    .foregroundStyle(.orange)
            } else {
                Button("Enable Location") {
                    locationManager.requestPermission()
                    locationManager.startUpdating()
                }
                .buttonStyle(.bordered)
            }

            TextField("Place name (optional)", text: $placeName)
                .textFieldStyle(.roundedBorder)

            MapReader { proxy in
                Map(position: $mapPosition) {
                    if let lat = latitude, let lon = longitude {
                        Marker(placeName.isEmpty ? "Selected" : placeName, coordinate: CLLocationCoordinate2D(latitude: lat, longitude: lon))
                    }
                }
                .frame(height: 180)
                .clipShape(RoundedRectangle(cornerRadius: 12))
                .onTapGesture { point in
                    if let coordinate = proxy.convert(point, from: .local) {
                        setCoordinate(coordinate)
                    }
                }
            }

            if latitude != nil {
                Text("Tap the map to adjust the pin.")
                    .font(.caption)
                    .foregroundStyle(.secondary)
            }
        }
        .onAppear {
            locationManager.startUpdating()
            if let lat = latitude, let lon = longitude {
                mapPosition = .region(MKCoordinateRegion(
                    center: CLLocationCoordinate2D(latitude: lat, longitude: lon),
                    span: MKCoordinateSpan(latitudeDelta: 0.05, longitudeDelta: 0.05)
                ))
            } else if let location = locationManager.currentLocation {
                mapPosition = .region(MKCoordinateRegion(
                    center: location.coordinate,
                    span: MKCoordinateSpan(latitudeDelta: 0.05, longitudeDelta: 0.05)
                ))
            }
        }
        .onChange(of: latitude) { _, _ in updateMapPosition() }
        .onChange(of: longitude) { _, _ in updateMapPosition() }
    }

    private func setCoordinate(_ coordinate: CLLocationCoordinate2D) {
        latitude = coordinate.latitude
        longitude = coordinate.longitude
        mapPosition = .region(MKCoordinateRegion(
            center: coordinate,
            span: MKCoordinateSpan(latitudeDelta: 0.05, longitudeDelta: 0.05)
        ))
    }

    private func updateMapPosition() {
        guard let lat = latitude, let lon = longitude else { return }
        mapPosition = .region(MKCoordinateRegion(
            center: CLLocationCoordinate2D(latitude: lat, longitude: lon),
            span: MKCoordinateSpan(latitudeDelta: 0.05, longitudeDelta: 0.05)
        ))
    }
}
