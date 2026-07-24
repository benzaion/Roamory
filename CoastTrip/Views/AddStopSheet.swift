import SwiftUI
import SwiftData

struct AddStopSheet: View {
    @Environment(\.modelContext) private var modelContext
    @Environment(\.dismiss) private var dismiss
    @EnvironmentObject private var locationManager: LocationManager

    let trip: Trip

    @State private var latitude: Double?
    @State private var longitude: Double?
    @State private var placeName = ""
    @State private var notes = ""
    @State private var selectedImages: [UIImage] = []
    @State private var showCamera = false
    @State private var errorMessage: String?

    var body: some View {
        NavigationStack {
            Form {
                Section("Location") {
                    LocationCaptureView(
                        latitude: $latitude,
                        longitude: $longitude,
                        placeName: $placeName
                    )
                }

                Section("Notes") {
                    TextField("What happened here?", text: $notes, axis: .vertical)
                        .lineLimit(3...6)
                }

                Section("Photos") {
                    PhotoPickerView(selectedImages: $selectedImages)
                    Button {
                        showCamera = true
                    } label: {
                        Label("Take Photo", systemImage: "camera.fill")
                    }
                }

                if let errorMessage {
                    Section {
                        Text(errorMessage)
                            .foregroundStyle(.red)
                    }
                }
            }
            .navigationTitle("Add Stop")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel") { dismiss() }
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button("Save") { saveStop() }
                        .disabled(latitude == nil || longitude == nil)
                }
            }
            .sheet(isPresented: $showCamera) {
                CameraPicker { image in
                    selectedImages.append(image)
                }
            }
            .onAppear {
                if let location = locationManager.currentLocation {
                    latitude = location.coordinate.latitude
                    longitude = location.coordinate.longitude
                }
            }
        }
    }

    private func saveStop() {
        guard let latitude, let longitude else { return }

        let stop = Stop(
            latitude: latitude,
            longitude: longitude,
            placeName: placeName.trimmingCharacters(in: .whitespacesAndNewlines),
            notes: notes.trimmingCharacters(in: .whitespacesAndNewlines)
        )
        stop.trip = trip
        trip.stops.append(stop)

        for image in selectedImages {
            do {
                let path = try PhotoStorageService.saveImage(image)
                let photo = TripPhoto(localFilePath: path)
                photo.stop = stop
                stop.photos.append(photo)
                modelContext.insert(photo)
            } catch {
                errorMessage = error.localizedDescription
            }
        }

        modelContext.insert(stop)
        try? modelContext.save()
        UIImpactFeedbackGenerator(style: .medium).impactOccurred()
        dismiss()
    }
}
