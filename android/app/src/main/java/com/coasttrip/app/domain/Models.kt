package com.coasttrip.app.domain

data class Trip(
    val id: String,
    val title: String,
    val startDateEpoch: Long,
    val endDateEpoch: Long?,
    val notes: String,
    val stops: List<Stop> = emptyList(),
    val highlights: List<Highlight> = emptyList(),
) {
    val sortedStops: List<Stop> get() = stops.sortedBy { it.timestampEpoch }
    val totalPhotos: Int get() = stops.sumOf { it.photos.size } + highlights.sumOf { it.photos.size }
    val totalMiles: Double get() = Distance.routeMiles(sortedStops.map { LatLngPoint(it.latitude, it.longitude) })
}

data class Stop(
    val id: String,
    val tripId: String,
    val latitude: Double,
    val longitude: Double,
    val placeName: String,
    val timestampEpoch: Long,
    val notes: String,
    val photos: List<TripPhoto> = emptyList(),
) {
    val locationLabel: String
        get() = placeName.ifBlank { String.format("%.4f, %.4f", latitude, longitude) }
}

data class Highlight(
    val id: String,
    val tripId: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val category: HighlightCategory,
    val notes: String,
    val rating: Int,
    val timestampEpoch: Long,
    val photos: List<TripPhoto> = emptyList(),
)

data class TripPhoto(
    val id: String,
    val stopId: String?,
    val highlightId: String?,
    val localFileName: String,
    val capturedAtEpoch: Long,
    val caption: String,
)

enum class HighlightCategory(val storage: String, val label: String) {
    Scenic("Scenic", "Scenic"),
    Food("Food", "Food"),
    Lodging("Lodging", "Lodging"),
    Landmark("Landmark", "Landmark"),
    Other("Other", "Other");

    companion object {
        fun fromStorage(value: String): HighlightCategory =
            entries.firstOrNull { it.storage == value } ?: Other
    }
}

sealed class TimelineItem {
    abstract val id: String
    abstract val timestampEpoch: Long
    abstract val title: String

    data class StopItem(val stop: Stop) : TimelineItem() {
        override val id: String get() = stop.id
        override val timestampEpoch: Long get() = stop.timestampEpoch
        override val title: String get() = stop.placeName.ifBlank { "Stop" }
    }

    data class HighlightItem(val highlight: Highlight) : TimelineItem() {
        override val id: String get() = highlight.id
        override val timestampEpoch: Long get() = highlight.timestampEpoch
        override val title: String get() = highlight.name
    }
}

fun Trip.timelineItems(): List<TimelineItem> {
    val stops = stops.map { TimelineItem.StopItem(it) }
    val highlights = highlights.map { TimelineItem.HighlightItem(it) }
    return (stops + highlights).sortedByDescending { it.timestampEpoch }
}
