import Foundation
import SwiftData
import CoreLocation
import SwiftUI

enum HighlightCategory: String, CaseIterable, Identifiable {
    case scenic = "Scenic"
    case food = "Food"
    case lodging = "Lodging"
    case landmark = "Landmark"
    case other = "Other"

    var id: String { rawValue }

    var color: Color {
        switch self {
        case .scenic: .green
        case .food: .orange
        case .lodging: .blue
        case .landmark: .purple
        case .other: .gray
        }
    }

    var icon: String {
        switch self {
        case .scenic: "leaf.fill"
        case .food: "fork.knife"
        case .lodging: "bed.double.fill"
        case .landmark: "star.fill"
        case .other: "mappin.circle.fill"
        }
    }
}

@Model
final class Highlight {
    var id: UUID
    var name: String
    var latitude: Double
    var longitude: Double
    var category: String
    var notes: String
    var rating: Int
    var timestamp: Date

    var trip: Trip?

    @Relationship(deleteRule: .cascade, inverse: \TripPhoto.highlight)
    var photos: [TripPhoto]

    init(
        id: UUID = UUID(),
        name: String,
        latitude: Double,
        longitude: Double,
        category: HighlightCategory = .other,
        notes: String = "",
        rating: Int = 3,
        timestamp: Date = .now
    ) {
        self.id = id
        self.name = name
        self.latitude = latitude
        self.longitude = longitude
        self.category = category.rawValue
        self.notes = notes
        self.rating = max(1, min(5, rating))
        self.timestamp = timestamp
        self.photos = []
    }

    var highlightCategory: HighlightCategory {
        HighlightCategory(rawValue: category) ?? .other
    }

    var coordinate: CLLocationCoordinate2D {
        CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
    }
}
