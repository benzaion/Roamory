import SwiftUI
import SwiftData
import MapKit

struct HighlightDetailView: View {
    @Environment(\.modelContext) private var modelContext
    @Environment(\.dismiss) private var dismiss

    @Bindable var highlight: Highlight

    @State private var isEditing = false
    @State private var showDeleteConfirm = false
    @State private var selectedCategory: HighlightCategory = .other
    @State private var selectedImages: [UIImage] = []
    @State private var showCamera = false

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                Map(initialPosition: .region(MKCoordinateRegion(
                    center: highlight.coordinate,
                    span: MKCoordinateSpan(latitudeDelta: 0.08, longitudeDelta: 0.08)
                ))) {
                    Annotation(highlight.name, coordinate: highlight.coordinate) {
                        Image(systemName: highlight.highlightCategory.icon)
                            .padding(8)
                            .background(highlight.highlightCategory.color)
                            .foregroundStyle(.white)
                            .clipShape(Circle())
                    }
                }
                .frame(height: 200)
                .clipShape(RoundedRectangle(cornerRadius: 12))

                VStack(alignment: .leading, spacing: 8) {
                    HStack {
                        Image(systemName: highlight.highlightCategory.icon)
                            .foregroundStyle(highlight.highlightCategory.color)
                        Text(highlight.highlightCategory.rawValue)
                            .font(.subheadline)
                            .foregroundStyle(.secondary)
                    }

                    if isEditing {
                        TextField("Name", text: $highlight.name)
                            .textFieldStyle(.roundedBorder)
                        Picker("Category", selection: $selectedCategory) {
                            ForEach(HighlightCategory.allCases) { cat in
                                Text(cat.rawValue).tag(cat)
                            }
                        }
                        Stepper("Rating: \(highlight.rating) ★", value: $highlight.rating, in: 1...5)
                    } else {
                        Text(highlight.name)
                            .font(.title2.bold())
                        HStack(spacing: 2) {
                            ForEach(1...5, id: \.self) { star in
                                Image(systemName: star <= highlight.rating ? "star.fill" : "star")
                                    .foregroundStyle(.yellow)
                            }
                        }
                    }

                    Text(highlight.timestamp.formatted(date: .complete, time: .shortened))
                        .foregroundStyle(.secondary)
                }

                if isEditing {
                    TextField("Notes", text: $highlight.notes, axis: .vertical)
                        .textFieldStyle(.roundedBorder)
                        .lineLimit(3...8)
                } else if !highlight.notes.isEmpty {
                    VStack(alignment: .leading, spacing: 4) {
                        Text("Notes")
                            .font(.headline)
                        Text(highlight.notes)
                    }
                }

                PhotoGallerySection(
                    photos: highlight.photos,
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
        .navigationTitle("Highlight")
        .navigationBarTitleDisplayMode(.inline)
        .onAppear {
            selectedCategory = highlight.highlightCategory
        }
        .toolbar {
            ToolbarItem(placement: .primaryAction) {
                Button(isEditing ? "Done" : "Edit") {
                    if isEditing {
                        highlight.category = selectedCategory.rawValue
                        saveEdits()
                    } else {
                        selectedCategory = highlight.highlightCategory
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
        .confirmationDialog("Delete this highlight?", isPresented: $showDeleteConfirm, titleVisibility: .visible) {
            Button("Delete", role: .destructive) {
                deleteHighlight()
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
                photo.highlight = highlight
                highlight.photos.append(photo)
                modelContext.insert(photo)
            }
        }
        try? modelContext.save()
    }

    private func deletePhoto(_ photo: TripPhoto) {
        PhotoStorageService.deleteImage(at: photo.localFilePath)
        highlight.photos.removeAll { $0.id == photo.id }
        modelContext.delete(photo)
        try? modelContext.save()
    }

    private func deleteHighlight() {
        for photo in highlight.photos {
            PhotoStorageService.deleteImage(at: photo.localFilePath)
        }
        if let trip = highlight.trip {
            trip.highlights.removeAll { $0.id == highlight.id }
        }
        modelContext.delete(highlight)
        try? modelContext.save()
        dismiss()
    }
}

struct PhotoGallerySection: View {
    let photos: [TripPhoto]
    let isEditing: Bool
    let onDelete: (TripPhoto) -> Void
    let onAddImages: ([UIImage]) -> Void

    private let columns = [GridItem(.flexible()), GridItem(.flexible()), GridItem(.flexible())]

    var body: some View {
        if !photos.isEmpty {
            VStack(alignment: .leading, spacing: 8) {
                Text("Photos")
                    .font(.headline)

                LazyVGrid(columns: columns, spacing: 8) {
                    ForEach(photos, id: \.id) { photo in
                        ZStack(alignment: .topTrailing) {
                            if let image = photo.image {
                                Image(uiImage: image)
                                    .resizable()
                                    .scaledToFill()
                                    .frame(height: 100)
                                    .clipShape(RoundedRectangle(cornerRadius: 8))
                            }

                            if isEditing {
                                Button {
                                    onDelete(photo)
                                } label: {
                                    Image(systemName: "xmark.circle.fill")
                                        .foregroundStyle(.white, .red)
                                }
                                .padding(4)
                            }
                        }
                    }
                }
            }
        }
    }
}
