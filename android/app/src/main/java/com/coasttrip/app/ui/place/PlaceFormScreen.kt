package com.coasttrip.app.ui.place

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coasttrip.app.data.location.DeviceLocation
import com.coasttrip.app.domain.Highlight
import com.coasttrip.app.domain.HighlightCategory
import com.coasttrip.app.domain.Stop
import com.coasttrip.app.ui.components.formatCoord
import com.coasttrip.app.ui.newId
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceFormScreen(
    title: String,
    isHighlight: Boolean,
    initialStop: Stop?,
    initialHighlight: Highlight?,
    tripId: String,
    hasLocationPermission: Boolean,
    onRequestLocation: () -> Unit,
    onCurrentLocation: suspend () -> DeviceLocation?,
    onSaveStop: (Stop, List<Uri>) -> Unit,
    onSaveHighlight: (Highlight, List<Uri>) -> Unit,
    onClose: () -> Unit,
) {
    var name by remember {
        mutableStateOf(initialStop?.placeName ?: initialHighlight?.name.orEmpty())
    }
    var notes by remember { mutableStateOf(initialStop?.notes ?: initialHighlight?.notes.orEmpty()) }
    var category by remember { mutableStateOf(initialHighlight?.category ?: HighlightCategory.Scenic) }
    var rating by remember { mutableIntStateOf(initialHighlight?.rating ?: 3) }
    var lat by remember { mutableStateOf(initialStop?.latitude ?: initialHighlight?.latitude) }
    var lng by remember { mutableStateOf(initialStop?.longitude ?: initialHighlight?.longitude) }
    var photos by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val scope = rememberCoroutineScope()
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) {
        photos = it
    }
    val canSave = lat != null && lng != null && (!isHighlight || name.isNotBlank())
    val camera = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(lat ?: 39.8283, lng ?: -98.5795),
            if (lat == null) 3f else 12f,
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onClose) { Icon(Icons.Filled.Close, contentDescription = "Close") }
                },
                actions = {
                    TextButton(
                        enabled = canSave,
                        onClick = {
                            val latitude = lat ?: return@TextButton
                            val longitude = lng ?: return@TextButton
                            if (isHighlight) {
                                onSaveHighlight(
                                    Highlight(
                                        id = initialHighlight?.id ?: newId(),
                                        tripId = tripId,
                                        name = name.trim(),
                                        latitude = latitude,
                                        longitude = longitude,
                                        category = category,
                                        notes = notes.trim(),
                                        rating = rating,
                                        timestampEpoch = initialHighlight?.timestampEpoch ?: System.currentTimeMillis(),
                                        photos = initialHighlight?.photos.orEmpty(),
                                    ),
                                    photos,
                                )
                            } else {
                                onSaveStop(
                                    Stop(
                                        id = initialStop?.id ?: newId(),
                                        tripId = tripId,
                                        latitude = latitude,
                                        longitude = longitude,
                                        placeName = name.trim(),
                                        timestampEpoch = initialStop?.timestampEpoch ?: System.currentTimeMillis(),
                                        notes = notes.trim(),
                                        photos = initialStop?.photos.orEmpty(),
                                    ),
                                    photos,
                                )
                            }
                        },
                    ) { Text("Save") }
                },
            )
        },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (isHighlight) {
                OutlinedTextField(value = name, onValueChange = { name = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Name") })
                Text("Category", style = MaterialTheme.typography.titleSmall)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    HighlightCategory.entries.forEach { option ->
                        FilterChip(selected = category == option, onClick = { category = option }, label = { Text(option.label) })
                    }
                }
                Row {
                    (1..5).forEach { star ->
                        IconButton(onClick = { rating = star }) {
                            Icon(
                                if (star <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = "$star stars",
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Location", style = MaterialTheme.typography.titleSmall)
                    if (lat != null && lng != null) {
                        Text(formatCoord(lat!!, lng!!), color = MaterialTheme.colorScheme.primary)
                    } else {
                        Text("Location is off. Tap the map, or enable location.", color = MaterialTheme.colorScheme.error)
                    }
                    Button(onClick = {
                        if (!hasLocationPermission) {
                            onRequestLocation()
                        } else {
                            scope.launch {
                                val loc = onCurrentLocation()
                                if (loc != null) {
                                    lat = loc.latitude
                                    lng = loc.longitude
                                    if (!isHighlight && name.isBlank()) name = "Current location"
                                    camera.position = CameraPosition.fromLatLngZoom(LatLng(loc.latitude, loc.longitude), 13f)
                                }
                            }
                        }
                    }) { Text("Use current location") }
                    if (!isHighlight) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Place name") },
                        )
                    }
                    GoogleMap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        cameraPositionState = camera,
                        onMapClick = { point ->
                            lat = point.latitude
                            lng = point.longitude
                        },
                    ) {
                        if (lat != null && lng != null) {
                            Marker(state = MarkerState(LatLng(lat!!, lng!!)), title = name.ifBlank { "Selected" })
                        }
                    }
                    Text("Tap the map to adjust the pin.", style = MaterialTheme.typography.bodySmall)
                }
            }
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(if (isHighlight) "Why is this place special?" else "What happened here?") },
                minLines = 3,
            )
            Text("Photos", style = MaterialTheme.typography.titleSmall)
            OutlinedButton(onClick = {
                picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }) { Text("Choose photos") }
            if (photos.isNotEmpty()) {
                Text("${photos.size} photo(s) selected", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
