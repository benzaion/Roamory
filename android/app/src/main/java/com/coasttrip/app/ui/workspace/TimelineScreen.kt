package com.coasttrip.app.ui.workspace

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coasttrip.app.domain.Highlight
import com.coasttrip.app.domain.Stop
import com.coasttrip.app.domain.TimelineItem
import com.coasttrip.app.domain.Trip
import com.coasttrip.app.domain.timelineItems
import com.coasttrip.app.ui.components.EmptyState
import com.coasttrip.app.ui.components.formatDate
import com.coasttrip.app.ui.components.formatDateTime
import java.time.Instant
import java.time.ZoneId

private enum class TimelineFilter { All, Stops, Highlights }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(
    trip: Trip,
    onOpenStop: (Stop) -> Unit,
    onOpenHighlight: (Highlight) -> Unit,
    onLogStop: () -> Unit,
) {
    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf(TimelineFilter.All) }
    val items = trip.timelineItems().filter { item ->
        val matchesFilter = when (filter) {
            TimelineFilter.All -> true
            TimelineFilter.Stops -> item is TimelineItem.StopItem
            TimelineFilter.Highlights -> item is TimelineItem.HighlightItem
        }
        val haystack = when (item) {
            is TimelineItem.StopItem -> "${item.stop.placeName} ${item.stop.notes}"
            is TimelineItem.HighlightItem -> "${item.highlight.name} ${item.highlight.notes} ${item.highlight.category.label}"
        }
        matchesFilter && (query.isBlank() || haystack.contains(query, true))
    }
    val groups = items.groupBy {
        Instant.ofEpochMilli(it.timestampEpoch).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Timeline") })
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = { Text("Search places, notes, food") },
            singleLine = true,
        )
        LazyRow(Modifier.padding(16.dp)) {
            items(TimelineFilter.entries) { option ->
                FilterChip(
                    selected = filter == option,
                    onClick = { filter = option },
                    label = { Text(option.name) },
                    modifier = Modifier.padding(end = 8.dp),
                )
            }
        }
        if (items.isEmpty()) {
            EmptyState(
                title = if (trip.timelineItems().isEmpty()) "No entries yet" else "No matches",
                message = if (trip.timelineItems().isEmpty()) {
                    "Stops and highlights show up here by day."
                } else {
                    "Try another search or filter."
                },
                action = "Log stop",
                onAction = onLogStop,
            )
        } else {
            LazyColumn(Modifier.fillMaxSize()) {
                groups.forEach { (day, rows) ->
                    item(key = "h-$day") {
                        Text(
                            formatDate(day.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()),
                            style = androidx.compose.material3.MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        )
                    }
                    items(rows, key = { it.id }) { item ->
                        ListItem(
                            headlineContent = { Text(item.title) },
                            supportingContent = {
                                Text(
                                    when (item) {
                                        is TimelineItem.StopItem -> item.stop.locationLabel
                                        is TimelineItem.HighlightItem -> item.highlight.category.label
                                    },
                                )
                            },
                            overlineContent = { Text(formatDateTime(item.timestampEpoch)) },
                            modifier = Modifier.clickable {
                                when (item) {
                                    is TimelineItem.StopItem -> onOpenStop(item.stop)
                                    is TimelineItem.HighlightItem -> onOpenHighlight(item.highlight)
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}
