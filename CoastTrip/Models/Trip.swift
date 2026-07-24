import Foundation
import SwiftData

@Model
final class Trip {
    var id: UUID
    var title: String
    var startDate: Date
    var endDate: Date?
    var notes: String

    @Relationship(deleteRule: .cascade, inverse: \Stop.trip)
    var stops: [Stop]

    @Relationship(deleteRule: .cascade, inverse: \Highlight.trip)
    var highlights: [Highlight]

    init(
        id: UUID = UUID(),
        title: String,
        startDate: Date = .now,
        endDate: Date? = nil,
        notes: String = ""
    ) {
        self.id = id
        self.title = title
        self.startDate = startDate
        self.endDate = endDate
        self.notes = notes
        self.stops = []
        self.highlights = []
    }

    var sortedStops: [Stop] {
        stops.sorted { $0.timestamp < $1.timestamp }
    }

    var totalPhotos: Int {
        stops.reduce(0) { $0 + $1.photos.count } + highlights.reduce(0) { $0 + $1.photos.count }
    }

    var totalMiles: Double {
        let sorted = sortedStops
        guard sorted.count > 1 else { return 0 }
        var miles: Double = 0
        for index in 1..<sorted.count {
            miles += sorted[index - 1].distance(to: sorted[index])
        }
        return miles
    }
}
