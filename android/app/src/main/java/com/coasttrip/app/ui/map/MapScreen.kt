package com.coasttrip.app.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.ZoomOutMap
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.coasttrip.app.BuildConfig
import com.coasttrip.app.domain.Highlight
import com.coasttrip.app.domain.HighlightCategory
import com.coasttrip.app.domain.Stop
import com.coasttrip.app.domain.Trip
import com.coasttrip.app.ui.components.formatDateTime
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

private enum class MapFilter { All, Stops, Highlights, Scenic, Food, Lodging, Landmark, Other }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    trip: Trip,
    onOpenStop: (Stop) -> Unit,
    onOpenHighlight: (Highlight) -> Unit,
    onDirections: (Double, Double) -> Unit,
    onMyLocation: () -> Unit,
) {
    var filter by remember { mutableStateOf(MapFilter.All) }
    var selectedStop by remember { mutableStateOf<Stop?>(null) }
    var selectedHighlight by remember { mutableStateOf<Highlight?>(null) }
    val stops = trip.sortedStops.filter { filter == MapFilter.All || filter == MapFilter.Stops }
    val highlights = trip.highlights.filter {
        when (filter) {
            MapFilter.All, MapFilter.Highlights -> true
            MapFilter.Stops -> false
            MapFilter.Scenic -> it.category == HighlightCategory.Scenic
            MapFilter.Food -> it.category == HighlightCategory.Food
            MapFilter.Lodging -> it.category == HighlightCategory.Lodging
            MapFilter.Landmark -> it.category == HighlightCategory.Landmark
            MapFilter.Other -> it.category == HighlightCategory.Other
        }
    }
    val useGoogle = BuildConfig.MAPS_API_KEY.isNotBlank()
    val usCenter = LatLng(39.8283, -98.5795)
    val first = trip.sortedStops.firstOrNull()?.let { LatLng(it.latitude, it.longitude) }
        ?: trip.highlights.firstOrNull()?.let { LatLng(it.latitude, it.longitude) }
        ?: usCenter
    val camera = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(first, if (trip.stops.isEmpty() && trip.highlights.isEmpty()) 3f else 5f)
    }

    Box(Modifier.fillMaxSize()) {
        if (useGoogle) {
            GoogleMap(modifier = Modifier.fillMaxSize(), cameraPositionState = camera) {
                if (stops.size > 1) {
                    Polyline(points = stops.map { LatLng(it.latitude, it.longitude) }, color = Color(0xFF3A72FB), width = 8f)
                }
                stops.forEach { stop ->
                    Marker(
                        state = MarkerState(LatLng(stop.latitude, stop.longitude)),
                        title = stop.locationLabel,
                        onClick = { selectedStop = stop; selectedHighlight = null; true },
                    )
                }
                highlights.forEach { highlight ->
                    Marker(
                        state = MarkerState(LatLng(highlight.latitude, highlight.longitude)),
                        title = highlight.name,
                        snippet = highlight.category.label,
                        onClick = { selectedHighlight = highlight; selectedStop = null; true },
                    )
                }
            }
        } else {
            FallbackMap(stops, highlights, onStop = { selectedStop = it; selectedHighlight = null }, onHighlight = { selectedHighlight = it; selectedStop = null })
        }

        Column(Modifier.fillMaxWidth()) {
            TopAppBar(title = { Text("Map") })
            LazyRow(Modifier.padding(horizontal = 12.dp)) {
                items(MapFilter.entries) { option ->
                    FilterChip(
                        selected = filter == option,
                        onClick = { filter = option },
                        label = { Text(option.name) },
                        modifier = Modifier.padding(end = 8.dp),
                    )
                }
            }
        }
        Column(
            Modifier
                .align(Alignment.TopEnd)
                .padding(top = 108.dp, end = 16.dp),
        ) {
            Surface(shape = CircleShape, tonalElevation = 2.dp) {
                IconButton(onClick = onMyLocation) { Icon(Icons.Outlined.MyLocation, contentDescription = "My location") }
            }
            Surface(shape = CircleShape, tonalElevation = 2.dp, modifier = Modifier.padding(top = 8.dp)) {
                IconButton(onClick = {
                    val target = trip.sortedStops.lastOrNull()?.let { LatLng(it.latitude, it.longitude) } ?: first
                    camera.position = CameraPosition.fromLatLngZoom(target, 6f)
                }) { Icon(Icons.Outlined.ZoomOutMap, contentDescription = "Fit route") }
            }
        }
    }

    val sheetItemStop = selectedStop
    val sheetItemHighlight = selectedHighlight
    if (sheetItemStop != null || sheetItemHighlight != null) {
        ModalBottomSheet(
            onDismissRequest = { selectedStop = null; selectedHighlight = null },
            sheetState = rememberModalBottomSheetState(),
        ) {
            val title = sheetItemStop?.locationLabel ?: sheetItemHighlight?.name.orEmpty()
            val support = sheetItemStop?.let { formatDateTime(it.timestampEpoch) }
                ?: "${sheetItemHighlight?.category?.label} · ${sheetItemHighlight?.rating} stars"
            val lat = sheetItemStop?.latitude ?: sheetItemHighlight!!.latitude
            val lng = sheetItemStop?.longitude ?: sheetItemHighlight!!.longitude
            Column(Modifier.padding(20.dp)) {
                Text(title, style = MaterialTheme.typography.titleLarge)
                Text(support, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(Modifier.padding(top = 16.dp)) {
                    Button(onClick = {
                        sheetItemStop?.let(onOpenStop)
                        sheetItemHighlight?.let(onOpenHighlight)
                        selectedStop = null
                        selectedHighlight = null
                    }, modifier = Modifier.padding(end = 8.dp)) { Text("Open") }
                    OutlinedButton(onClick = { onDirections(lat, lng) }) { Text("Directions") }
                }
            }
        }
    }
}

@Composable
private fun FallbackMap(
    stops: List<Stop>,
    highlights: List<Highlight>,
    onStop: (Stop) -> Unit,
    onHighlight: (Highlight) -> Unit,
) {
    val all = stops.map { it.latitude to it.longitude } + highlights.map { it.latitude to it.longitude }
    val minLat = all.minOfOrNull { it.first } ?: 24.0
    val maxLat = all.maxOfOrNull { it.first } ?: 50.0
    val minLng = all.minOfOrNull { it.second } ?: -125.0
    val maxLng = all.maxOfOrNull { it.second } ?: -66.0
    fun x(lng: Double) = ((lng - minLng) / (maxLng - minLng).coerceAtLeast(0.01)).toFloat()
    fun y(lat: Double) = (1f - ((lat - minLat) / (maxLat - minLat).coerceAtLeast(0.01)).toFloat())

    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF8AA56A)),
    ) {
        Text(
            "Maps fallback — add MAPS_API_KEY to local.properties for Google Maps",
            color = Color.White,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(12.dp),
        )
        stops.forEach { stop ->
            Icon(
                Icons.Outlined.Place,
                contentDescription = stop.locationLabel,
                tint = Color(0xFF3A72FB),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (x(stop.longitude) * 320).dp, y = (y(stop.latitude) * 520).dp)
                    .size(28.dp)
                    .clickable { onStop(stop) },
            )
        }
        highlights.forEach { highlight ->
            Box(
                Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (x(highlight.longitude) * 320).dp, y = (y(highlight.latitude) * 520).dp)
                    .size(22.dp)
                    .background(Color(0xFF7B1FA2), CircleShape)
                    .clickable { onHighlight(highlight) },
            )
        }
    }
}
