import UIKit

enum PhotoStorageService {
    private static var photosDirectory: URL {
        let documents = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)[0]
        let photos = documents.appendingPathComponent("Photos", isDirectory: true)
        if !FileManager.default.fileExists(atPath: photos.path) {
            try? FileManager.default.createDirectory(at: photos, withIntermediateDirectories: true)
        }
        return photos
    }

    @discardableResult
    static func saveImage(_ image: UIImage, quality: CGFloat = 0.85) throws -> String {
        let filename = UUID().uuidString + ".jpg"
        let fileURL = photosDirectory.appendingPathComponent(filename)
        guard let data = image.jpegData(compressionQuality: quality) else {
            throw PhotoStorageError.compressionFailed
        }
        try data.write(to: fileURL, options: .atomic)
        return filename
    }

    static func loadImage(from filename: String) -> UIImage? {
        let fileURL = photosDirectory.appendingPathComponent(filename)
        guard FileManager.default.fileExists(atPath: fileURL.path) else { return nil }
        return UIImage(contentsOfFile: fileURL.path)
    }

    static func deleteImage(at filename: String) {
        let fileURL = photosDirectory.appendingPathComponent(filename)
        try? FileManager.default.removeItem(at: fileURL)
    }

    static func fullURL(for filename: String) -> URL {
        photosDirectory.appendingPathComponent(filename)
    }
}

enum PhotoStorageError: LocalizedError {
    case compressionFailed

    var errorDescription: String? {
        switch self {
        case .compressionFailed:
            "Could not compress the photo for storage."
        }
    }
}
