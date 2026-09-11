package com.coasttrip.app.ui.workspace

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.coasttrip.app.domain.Distance
import com.coasttrip.app.domain.DistanceUnit
import com.coasttrip.app.domain.Highlight
import com.coasttrip.app.domain.Stop
import com.coasttrip.app.domain.TimelineItem
import com.coasttrip.app.domain.Trip
import com.coasttrip.app.domain.timelineItems
import com.coasttrip.app.ui.components.EmptyState
import com.coasttrip.app.ui.components.formatDate
import com.coasttrip.app.ui.components.formatDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    trip: Trip,
    units: DistanceUnit,
    onBack: () -> Unit,
    onSettings: () -> Unit,
    onOpenStop: (Stop) -> Unit,
    onOpenHighlight: (Highlight) -> Unit,
    onLogStop: () -> Unit,
) {
    val empty = trip.stops.isEmpty() && trip.highlights.isEmpty()
    val (distance, label) = Distance.format(trip.totalMiles, units)
    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Trip") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "All trips")
                }
            },
            actions = {
                IconButton(onClick = onSettings) {
                    Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                }
            },
        )
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            Text(trip.title, style = MaterialTheme.typography.headlineMedium)
            Text(
                "${formatDate(trip.startDateEpoch)}${if (trip.endDateEpoch == null) " · Open" else ""}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StatCard("Stops", "${trip.stops.size}", Icons.Outlined.Place, Modifier.weight(1f))
                StatCard("Highlights", "${trip.highlights.size}", Icons.Outlined.Star, Modifier.weight(1f))
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StatCard("Photos", "${trip.totalPhotos}", Icons.Outlined.Photo, Modifier.weight(1f))
                StatCard(label, "$distance", Icons.Outlined.Route, Modifier.weight(1f))
            }
            if (empty) {
                EmptyState(
                    title = "No stops yet",
                    message = "Log a stop or highlight as you drive. Everything stays on this device.",
                    action = "Log stop",
                    onAction = onLogStop,
                )
            } else {
                Text("Recent activity", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 24.dp, bottom = 8.dp))
                trip.timelineItems().take(5).forEach { item ->
                    ActivityRow(item, onOpenStop, onOpenHighlight)
                }
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(Modifier.padding(14.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(value, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(top = 8.dp))
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ActivityRow(
    item: TimelineItem,
    onOpenStop: (Stop) -> Unit,
    onOpenHighlight: (Highlight) -> Unit,
) {
    val onClick = when (item) {
        is TimelineItem.StopItem -> ({ onOpenStop(item.stop) })
        is TimelineItem.HighlightItem -> ({ onOpenHighlight(item.highlight) })
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(item.title, style = MaterialTheme.typography.titleSmall)
            Text(formatDateTime(item.timestampEpoch), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
