import SwiftUI

struct HomeView: View {
    let trip: Trip
    let onAddStop: () -> Void
    let onAddHighlight: () -> Void

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(alignment: .leading, spacing: 20) {
                    VStack(alignment: .leading, spacing: 4) {
                        Text(trip.title)
                            .font(.largeTitle.bold())
                        Text(trip.startDate.formatted(date: .abbreviated, time: .omitted))
                            .foregroundStyle(.secondary)
                    }

                    LazyVGrid(columns: [GridItem(.flexible()), GridItem(.flexible())], spacing: 12) {
                        TripStatCard(title: "Stops", value: "\(trip.stops.count)", icon: "mappin.and.ellipse", color: .blue)
                        TripStatCard(title: "Highlights", value: "\(trip.highlights.count)", icon: "star.fill", color: .purple)
                        TripStatCard(title: "Photos", value: "\(trip.totalPhotos)", icon: "photo.fill", color: .orange)
                        TripStatCard(title: "Miles", value: String(format: "%.0f", trip.totalMiles), icon: "road.lanes", color: .green)
                    }

                    if trip.stops.isEmpty && trip.highlights.isEmpty {
                        ContentUnavailableView {
                            Label("No stops yet", systemImage: "map")
                        } description: {
                            Text("Tap below to log your first stop or highlight along the route.")
                        }
                    } else {
                        VStack(alignment: .leading, spacing: 12) {
                            Text("Recent Activity")
                                .font(.headline)

                            ForEach(recentItems.prefix(5), id: \.id) { item in
                                NavigationLink {
                                    destination(for: item)
                                } label: {
                                    RecentActivityRow(item: item)
                                }
                                .buttonStyle(.plain)
                            }
                        }
                    }

                    VStack(spacing: 12) {
                        Button(action: onAddStop) {
                            Label("Add Stop", systemImage: "plus.circle.fill")
                                .frame(maxWidth: .infinity)
                        }
                        .buttonStyle(.borderedProminent)
                        .controlSize(.large)

                        Button(action: onAddHighlight) {
                            Label("Add Highlight", systemImage: "star.circle.fill")
                                .frame(maxWidth: .infinity)
                        }
                        .buttonStyle(.bordered)
                        .controlSize(.large)
                    }
                }
                .padding()
            }
            .navigationTitle("Trip")
            .navigationBarTitleDisplayMode(.inline)
        }
    }

    private var recentItems: [TimelineItem] {
        let stopItems = trip.stops.map { TimelineItem.stop($0) }
        let highlightItems = trip.highlights.map { TimelineItem.highlight($0) }
        return (stopItems + highlightItems).sorted { $0.timestamp > $1.timestamp }
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

private struct RecentActivityRow: View {
    let item: TimelineItem

    var body: some View {
        HStack(spacing: 12) {
            Image(systemName: item.iconName)
                .foregroundStyle(item.tint)
                .frame(width: 32)

            VStack(alignment: .leading, spacing: 2) {
                Text(item.title)
                    .font(.subheadline.weight(.medium))
                Text(item.timestamp.formatted(date: .abbreviated, time: .shortened))
                    .font(.caption)
                    .foregroundStyle(.secondary)
            }

            Spacer()

            if let thumbnail = item.thumbnail {
                Image(uiImage: thumbnail)
                    .resizable()
                    .scaledToFill()
                    .frame(width: 44, height: 44)
                    .clipShape(RoundedRectangle(cornerRadius: 6))
            }
        }
        .padding(12)
        .background(Color(.secondarySystemBackground))
        .clipShape(RoundedRectangle(cornerRadius: 10))
    }
}
