import SwiftUI
import SwiftData
import MapKit

struct StopDetailView: View {
    @Environment(\.modelContext) private var modelContext
    @Environment(\.dismiss) private var dismiss

    @Bindable var stop: Stop

    @State private var isEditing = false
    @State private var showDeleteConfirm = false
    @State private var selectedImages: [UIImage] = []
    @State private var showCamera = false

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                Map(initialPosition: .region(MKCoordinateRegion(
                    center: stop.coordinate,
                    span: MKCoordinateSpan(latitudeDelta: 0.08, longitudeDelta: 0.08)
                ))) {
                    Marker(stop.locationLabel, coordinate: stop.coordinate)
                }
                .frame(height: 200)
                .clipShape(RoundedRectangle(cornerRadius: 12))

                VStack(alignment: .leading, spacing: 8) {
                    Text(stop.placeName.isEmpty ? "Stop" : stop.placeName)
                        .font(.title2.bold())
                    Text(stop.timestamp.formatted(date: .complete, time: .shortened))
                        .foregroundStyle(.secondary)
                    Text(stop.locationLabel)
                        .font(.subheadline)
                        .foregroundStyle(.secondary)
                }

                if isEditing {
                    TextField("Place name", text: $stop.placeName)
                        .textFieldStyle(.roundedBorder)
                    TextField("Notes", text: $stop.notes, axis: .vertical)
                        .textFieldStyle(.roundedBorder)
                        .lineLimit(3...8)
                } else if !stop.notes.isEmpty {
                    VStack(alignment: .leading, spacing: 4) {
                        Text("Notes")
                            .font(.headline)
                        Text(stop.notes)
                    }
                }

                PhotoGallerySection(
                    photos: stop.photos,
                    isEditing: isEditing,
                    onDelete: deletePhoto,
                    onAddImages: addPhotos
                )

                if isEditing {
                    PhotoPickerView(selectedImages: $selectedImages)
                    Button {
                        showCamera = true
                    } label: {
                        Label("Take Photo", systemImage: "camera.fill")
                    }
                }
            }
            .padding()
        }
        .navigationTitle("Stop")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
            ToolbarItem(placement: .primaryAction) {
                Button(isEditing ? "Done" : "Edit") {
                    if isEditing {
                        saveEdits()
                    }
                    isEditing.toggle()
                }
            }
            ToolbarItem(placement: .destructiveAction) {
                Button("Delete", role: .destructive) {
                    showDeleteConfirm = true
                }
            }
        }
        .confirmationDialog("Delete this stop?", isPresented: $showDeleteConfirm, titleVisibility: .visible) {
            Button("Delete", role: .destructive) {
                deleteStop()
            }
        }
        .sheet(isPresented: $showCamera) {
            CameraPicker { image in
                addPhotos([image])
            }
        }
    }

    private func saveEdits() {
        addPhotos(selectedImages)
        selectedImages = []
        try? modelContext.save()
    }

    private func addPhotos(_ images: [UIImage]) {
        for image in images {
            if let path = try? PhotoStorageService.saveImage(image) {
                let photo = TripPhoto(localFilePath: path)
                photo.stop = stop
                stop.photos.append(photo)
                modelContext.insert(photo)
            }
        }
        try? modelContext.save()
    }

    private func deletePhoto(_ photo: TripPhoto) {
        PhotoStorageService.deleteImage(at: photo.localFilePath)
        stop.photos.removeAll { $0.id == photo.id }
        modelContext.delete(photo)
        try? modelContext.save()
    }

    private func deleteStop() {
        for photo in stop.photos {
            PhotoStorageService.deleteImage(at: photo.localFilePath)
        }
        if let trip = stop.trip {
            trip.stops.removeAll { $0.id == stop.id }
        }
        modelContext.delete(stop)
        try? modelContext.save()
        dismiss()
    }
}
