import Foundation
import SwiftData
import CoreLocation

@Model
final class Stop {
    var id: UUID
    var latitude: Double
    var longitude: Double
    var placeName: String
    var timestamp: Date
    var notes: String

    var trip: Trip?

    @Relationship(deleteRule: .cascade, inverse: \TripPhoto.stop)
    var photos: [TripPhoto]

    init(
        id: UUID = UUID(),
        latitude: Double,
        longitude: Double,
        placeName: String = "",
        timestamp: Date = .now,
        notes: String = ""
    ) {
        self.id = id
        self.latitude = latitude
        self.longitude = longitude
        self.placeName = placeName
        self.timestamp = timestamp
        self.notes = notes
        self.photos = []
    }

    var coordinate: CLLocationCoordinate2D {
        CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
    }

    var locationLabel: String {
        if !placeName.isEmpty {
            return placeName
        }
        return String(format: "%.4f, %.4f", latitude, longitude)
    }

    func distance(to other: Stop) -> Double {
        let from = CLLocation(latitude: latitude, longitude: longitude)
        let to = CLLocation(latitude: other.latitude, longitude: other.longitude)
        return from.distance(from: to) / 1609.344
    }
}
