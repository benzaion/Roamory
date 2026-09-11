import Foundation
import SwiftData
import UIKit

@Model
final class TripPhoto {
    var id: UUID
    var localFilePath: String
    var capturedAt: Date
    var caption: String

    var stop: Stop?
    var highlight: Highlight?

    init(
        id: UUID = UUID(),
        localFilePath: String,
        capturedAt: Date = .now,
        caption: String = ""
    ) {
        self.id = id
        self.localFilePath = localFilePath
        self.capturedAt = capturedAt
        self.caption = caption
    }

    var image: UIImage? {
        PhotoStorageService.loadImage(from: localFilePath)
    }
}
