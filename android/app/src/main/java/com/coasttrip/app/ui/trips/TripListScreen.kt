package com.coasttrip.app.ui.trips

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coasttrip.app.domain.Distance
import com.coasttrip.app.domain.DistanceUnit
import com.coasttrip.app.domain.Trip
import com.coasttrip.app.ui.components.EmptyState
import com.coasttrip.app.ui.components.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripListScreen(
    trips: List<Trip>,
    units: DistanceUnit,
    onOpenTrip: (String) -> Unit,
    onCreateTrip: () -> Unit,
    onEditTrip: (String) -> Unit,
    onDeleteTrip: (String) -> Unit,
    onSettings: () -> Unit,
) {
    var query by remember { mutableStateOf("") }
    var pendingDelete by remember { mutableStateOf<Trip?>(null) }
    val filtered = trips.filter {
        query.isBlank() || it.title.contains(query, true) || it.notes.contains(query, true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trips") },
                actions = {
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                    }
                },
            )
        },
        floatingActionButton = {
            if (trips.isNotEmpty()) {
                FloatingActionButton(onClick = onCreateTrip) {
                    Icon(Icons.Filled.Add, contentDescription = "Start a trip")
                }
            }
        },
    ) { padding ->
        if (trips.isEmpty()) {
            EmptyState(
                title = "No trips yet",
                message = "Start a trip, then log stops as you drive. Everything stays on this device.",
                action = "Start a trip",
                onAction = onCreateTrip,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search trips") },
                        singleLine = true,
                    )
                }
                items(filtered, key = { it.id }) { trip ->
                    TripCard(
                        trip = trip,
                        units = units,
                        onOpen = { onOpenTrip(trip.id) },
                        onEdit = { onEditTrip(trip.id) },
                        onDelete = { pendingDelete = trip },
                    )
                }
            }
        }
    }

    pendingDelete?.let { trip ->
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = { Text("Delete this trip?") },
            text = { Text("Stops, highlights, and photos for this trip will be removed from the device.") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteTrip(trip.id)
                    pendingDelete = null
                }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { pendingDelete = null }) { Text("Cancel") }
            },
        )
    }
}

@Composable
private fun TripCard(
    trip: Trip,
    units: DistanceUnit,
    onOpen: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    var menu by remember { mutableStateOf(false) }
    val (value, label) = Distance.format(trip.totalMiles, units)
    val dates = buildString {
        append(formatDate(trip.startDateEpoch))
        append(if (trip.endDateEpoch == null) " · Open" else " – ${formatDate(trip.endDateEpoch)}")
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(Modifier.padding(start = 16.dp, end = 4.dp, top = 8.dp, bottom = 16.dp)) {
            Row(Modifier.fillMaxWidth()) {
                Column(
                    Modifier
                        .weight(1f)
                        .clickable(onClick = onOpen)
                        .padding(vertical = 8.dp),
                ) {
                    Text(trip.title, style = MaterialTheme.typography.titleMedium)
                    Text(dates, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                IconButton(onClick = { menu = true }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Trip options")
                }
                DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                    DropdownMenuItem(text = { Text("Edit") }, onClick = { menu = false; onEdit() })
                    DropdownMenuItem(text = { Text("Delete") }, onClick = { menu = false; onDelete() })
                }
            }
            Spacer(Modifier.padding(top = 4.dp))
            Text(
                "${trip.stops.size} stops · ${trip.highlights.size} highlights · $value ${label.lowercase()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
