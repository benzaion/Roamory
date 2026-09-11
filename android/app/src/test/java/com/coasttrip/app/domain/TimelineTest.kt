package com.coasttrip.app.domain

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TimelineTest {
    @Test
    fun timelineItems_sortNewestFirst_andMixStopsWithHighlights() {
        val trip = Trip(
            id = "t1",
            title = "Test",
            startDateEpoch = 0L,
            endDateEpoch = null,
            notes = "",
            stops = listOf(
                Stop("s1", "t1", 1.0, 1.0, "Older", 1_000L, ""),
                Stop("s2", "t1", 2.0, 2.0, "Newer", 3_000L, ""),
            ),
            highlights = listOf(
                Highlight("h1", "t1", "Mid", 1.5, 1.5, HighlightCategory.Food, "", 4, 2_000L),
            ),
        )
        val items = trip.timelineItems()
        assertThat(items.map { it.id }).containsExactly("s2", "h1", "s1").inOrder()
        assertThat(trip.totalPhotos).isEqualTo(0)
        assertThat(trip.totalMiles).isGreaterThan(0.0)
    }
}
