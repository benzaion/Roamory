import SwiftUI

enum TimelineItem: Identifiable {
    case stop(Stop)
    case highlight(Highlight)

    var id: UUID {
        switch self {
        case .stop(let stop): stop.id
        case .highlight(let highlight): highlight.id
        }
    }

    var timestamp: Date {
        switch self {
        case .stop(let stop): stop.timestamp
        case .highlight(let highlight): highlight.timestamp
        }
    }

    var title: String {
        switch self {
        case .stop(let stop): stop.placeName.isEmpty ? "Stop" : stop.placeName
        case .highlight(let highlight): highlight.name
        }
    }

    var subtitle: String {
        switch self {
        case .stop(let stop): stop.locationLabel
        case .highlight(let highlight): highlight.highlightCategory.rawValue
        }
    }

    var iconName: String {
        switch self {
        case .stop: "mappin.circle.fill"
        case .highlight(let highlight): highlight.highlightCategory.icon
        }
    }

    var tint: Color {
        switch self {
        case .stop: .blue
        case .highlight(let highlight): highlight.highlightCategory.color
        }
    }

    var thumbnail: UIImage? {
        switch self {
        case .stop(let stop): stop.photos.first?.image
        case .highlight(let highlight): highlight.photos.first?.image
        }
    }
}

struct TimelineView: View {
    let trip: Trip

    private var groupedItems: [(Date, [TimelineItem])] {
        let items = trip.stops.map { TimelineItem.stop($0) } + trip.highlights.map { TimelineItem.highlight($0) }
        let sorted = items.sorted { $0.timestamp > $1.timestamp }
        let calendar = Calendar.current
        let grouped = Dictionary(grouping: sorted) { calendar.startOfDay(for: $0.timestamp) }
        return grouped.sorted { $0.key > $1.key }
    }

    var body: some View {
        NavigationStack {
            Group {
                if groupedItems.isEmpty {
                    ContentUnavailableView {
                        Label("No entries yet", systemImage: "clock")
                    } description: {
                        Text("Your stops and highlights will appear here in chronological order.")
                    }
                } else {
                    List {
                        ForEach(groupedItems, id: \.0) { day, items in
                            Section(day.formatted(date: .complete, time: .omitted)) {
                                ForEach(items) { item in
                                    NavigationLink {
                                        destination(for: item)
                                    } label: {
                                        TimelineRow(item: item)
                                    }
                                }
                            }
                        }
                    }
                    .listStyle(.insetGrouped)
                }
            }
            .navigationTitle("Timeline")
        }
    }

    @ViewBuilder
    private func destination(for item: TimelineItem) -> some View {
        switch item {
        case .stop(let stop):
            StopDetailView(stop: stop)
        case .highlight(let highlight):
            HighlightDetailView(highlight: highlight)
        }
    }
}

private struct TimelineRow: View {
    let item: TimelineItem

    var body: some View {
        HStack(spacing: 12) {
            Image(systemName: item.iconName)
                .foregroundStyle(item.tint)
                .frame(width: 28)

            VStack(alignment: .leading, spacing: 4) {
                Text(item.title)
                    .font(.headline)
                Text(item.subtitle)
                    .font(.subheadline)
                    .foregroundStyle(.secondary)
                Text(item.timestamp.formatted(date: .omitted, time: .shortened))
                    .font(.caption)
                    .foregroundStyle(.tertiary)
            }

            Spacer()

            if let thumbnail = item.thumbnail {
                Image(uiImage: thumbnail)
                    .resizable()
                    .scaledToFill()
                    .frame(width: 56, height: 56)
                    .clipShape(RoundedRectangle(cornerRadius: 8))
            }
        }
        .padding(.vertical, 4)
    }
}
