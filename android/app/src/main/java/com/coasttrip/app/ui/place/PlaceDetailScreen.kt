package com.coasttrip.app.ui.place

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.coasttrip.app.domain.Highlight
import com.coasttrip.app.domain.Stop
import com.coasttrip.app.domain.TripPhoto
import com.coasttrip.app.ui.components.formatCoord
import com.coasttrip.app.ui.components.formatDateTime
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailScreen(
    stop: Stop?,
    highlight: Highlight?,
    photoFile: (TripPhoto) -> File,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDeletePhoto: (TripPhoto) -> Unit,
    onCaption: (TripPhoto, String) -> Unit,
    onBack: () -> Unit,
) {
    val title = if (stop != null) "Stop" else "Highlight"
    val name = stop?.placeName?.ifBlank { "Stop" } ?: highlight?.name.orEmpty()
    val lat = stop?.latitude ?: highlight!!.latitude
    val lng = stop?.longitude ?: highlight!!.longitude
    val notes = stop?.notes ?: highlight?.notes.orEmpty()
    val time = stop?.timestampEpoch ?: highlight!!.timestampEpoch
    val photos = stop?.photos ?: highlight!!.photos
    var menu by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }
    val camera = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(lat, lng), 12f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { menu = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More")
                    }
                    DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                        DropdownMenuItem(text = { Text("Edit") }, onClick = { menu = false; onEdit() })
                        DropdownMenuItem(text = { Text("Delete") }, onClick = { menu = false; confirmDelete = true })
                    }
                },
            )
        },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp),
                cameraPositionState = camera,
            ) {
                Marker(state = MarkerState(LatLng(lat, lng)), title = name)
            }
            Column(Modifier.padding(16.dp)) {
                highlight?.let {
                    Text(it.category.label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    Row {
                        repeat(5) { index ->
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = null,
                                tint = if (index < it.rating) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                            )
                        }
                    }
                }
                Text(name, style = MaterialTheme.typography.headlineSmall)
                Text(formatDateTime(time), color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (stop != null) {
                    Text(formatCoord(lat, lng), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (notes.isNotBlank()) {
                    Text("Notes", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
                    Text(notes)
                }
                if (photos.isNotEmpty()) {
                    Text("Photos", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
                    photos.forEach { photo ->
                        AsyncImage(
                            model = photoFile(photo),
                            contentDescription = photo.caption.ifBlank { name },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .padding(bottom = 8.dp),
                            contentScale = ContentScale.Crop,
                        )
                        if (photo.caption.isNotBlank()) {
                            Text(photo.caption, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Delete this ${title.lowercase()}?") },
            text = { Text("Photos stored for this ${title.lowercase()} will be removed from the device.") },
            confirmButton = {
                TextButton(onClick = { confirmDelete = false; onDelete() }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text("Cancel") } },
        )
    }
}
