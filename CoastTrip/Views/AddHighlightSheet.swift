import SwiftUI
import SwiftData

struct AddHighlightSheet: View {
    @Environment(\.modelContext) private var modelContext
    @Environment(\.dismiss) private var dismiss
    @EnvironmentObject private var locationManager: LocationManager

    let trip: Trip

    @State private var name = ""
    @State private var category: HighlightCategory = .scenic
    @State private var rating = 3
    @State private var latitude: Double?
    @State private var longitude: Double?
    @State private var notes = ""
    @State private var selectedImages: [UIImage] = []
    @State private var showCamera = false
    @State private var errorMessage: String?

    var body: some View {
        NavigationStack {
            Form {
                Section("Highlight") {
                    TextField("Name", text: $name)
                    Picker("Category", selection: $category) {
                        ForEach(HighlightCategory.allCases) { cat in
                            Label(cat.rawValue, systemImage: cat.icon).tag(cat)
                        }
                    }
                    Stepper("Rating: \(rating) ★", value: $rating, in: 1...5)
                }

                Section("Location") {
                    LocationCaptureView(
                        latitude: $latitude,
                        longitude: $longitude,
                        placeName: $name
                    )
                }

                Section("Notes") {
                    TextField("Why is this place special?", text: $notes, axis: .vertical)
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
            .navigationTitle("Add Highlight")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel") { dismiss() }
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button("Save") { saveHighlight() }
                        .disabled(name.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty || latitude == nil || longitude == nil)
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

    private func saveHighlight() {
        guard let latitude, let longitude else { return }

        let highlight = Highlight(
            name: name.trimmingCharacters(in: .whitespacesAndNewlines),
            latitude: latitude,
            longitude: longitude,
            category: category,
            notes: notes.trimmingCharacters(in: .whitespacesAndNewlines),
            rating: rating
        )
        highlight.trip = trip
        trip.highlights.append(highlight)

        for image in selectedImages {
            do {
                let path = try PhotoStorageService.saveImage(image)
                let photo = TripPhoto(localFilePath: path)
                photo.highlight = highlight
                highlight.photos.append(photo)
                modelContext.insert(photo)
            } catch {
                errorMessage = error.localizedDescription
            }
        }

        modelContext.insert(highlight)
        try? modelContext.save()
        UIImpactFeedbackGenerator(style: .medium).impactOccurred()
        dismiss()
    }
}
