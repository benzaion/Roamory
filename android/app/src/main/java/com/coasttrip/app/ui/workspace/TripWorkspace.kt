package com.coasttrip.app.ui.workspace

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.AddLocation
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coasttrip.app.domain.DistanceUnit
import com.coasttrip.app.domain.Highlight
import com.coasttrip.app.domain.Stop
import com.coasttrip.app.ui.TripViewModel
import com.coasttrip.app.ui.map.MapScreen
import com.coasttrip.app.ui.place.PlaceDetailScreen
import com.coasttrip.app.ui.place.PlaceFormScreen
import kotlinx.coroutines.launch

private enum class WorkspaceTab { Home, Map, Timeline }
private enum class Overlay { None, AddStop, AddHighlight, StopDetail, HighlightDetail, EditStop, EditHighlight }

@Composable
fun TripWorkspace(
    tripViewModel: TripViewModel,
    units: DistanceUnit,
    onBackToTrips: () -> Unit,
    onTripSettings: () -> Unit,
) {
    val trip by tripViewModel.trip.collectAsStateWithLifecycle()
    val current = trip ?: return
    var tab by remember { mutableStateOf(WorkspaceTab.Home) }
    var overlay by remember { mutableStateOf(Overlay.None) }
    var selectedStop by remember { mutableStateOf<Stop?>(null) }
    var selectedHighlight by remember { mutableStateOf<Highlight?>(null) }
    var fabOpen by remember { mutableStateOf(false) }
    var showLocationRationale by remember { mutableStateOf(false) }
    var pendingLocationAction by remember { mutableStateOf<(() -> Unit)?>(null) }
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val locationPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { granted ->
        if (granted.values.any { it }) pendingLocationAction?.invoke()
        pendingLocationAction = null
    }

    fun requestLocation(then: () -> Unit) {
        if (tripViewModel.hasLocationPermission()) {
            then()
        } else {
            pendingLocationAction = then
            showLocationRationale = true
        }
    }

    fun openDirections(lat: Double, lng: Double) {
        val nav = Uri.parse("google.navigation:q=$lat,$lng")
        val geo = Uri.parse("geo:$lat,$lng")
        val intent = Intent(Intent.ACTION_VIEW, nav).apply { setPackage("com.google.android.apps.maps") }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            context.startActivity(Intent(Intent.ACTION_VIEW, geo))
        }
    }

    when (overlay) {
        Overlay.AddStop, Overlay.EditStop -> {
            PlaceFormScreen(
                title = if (overlay == Overlay.EditStop) "Edit stop" else "Log stop",
                isHighlight = false,
                initialStop = if (overlay == Overlay.EditStop) selectedStop else null,
                initialHighlight = null,
                tripId = current.id,
                hasLocationPermission = tripViewModel.hasLocationPermission(),
                onRequestLocation = { requestLocation {} },
                onCurrentLocation = { tripViewModel.currentLocation() },
                onSaveStop = { stop, uris ->
                    tripViewModel.saveStop(stop, uris)
                    overlay = Overlay.None
                    scope.launch { snackbar.showSnackbar("Stop saved") }
                },
                onSaveHighlight = { _, _ -> },
                onClose = { overlay = Overlay.None },
            )
            return
        }
        Overlay.AddHighlight, Overlay.EditHighlight -> {
            PlaceFormScreen(
                title = if (overlay == Overlay.EditHighlight) "Edit highlight" else "Add highlight",
                isHighlight = true,
                initialStop = null,
                initialHighlight = if (overlay == Overlay.EditHighlight) selectedHighlight else null,
                tripId = current.id,
                hasLocationPermission = tripViewModel.hasLocationPermission(),
                onRequestLocation = { requestLocation {} },
                onCurrentLocation = { tripViewModel.currentLocation() },
                onSaveStop = { _, _ -> },
                onSaveHighlight = { highlight, uris ->
                    tripViewModel.saveHighlight(highlight, uris)
                    overlay = Overlay.None
                    scope.launch { snackbar.showSnackbar("Highlight saved") }
                },
                onClose = { overlay = Overlay.None },
            )
            return
        }
        Overlay.StopDetail -> selectedStop?.let { stop ->
            PlaceDetailScreen(
                stop = stop,
                highlight = null,
                photoFile = tripViewModel::photoFile,
                onEdit = { overlay = Overlay.EditStop },
                onDelete = {
                    tripViewModel.deleteStop(stop)
                    overlay = Overlay.None
                    scope.launch { snackbar.showSnackbar("Stop deleted") }
                },
                onDeletePhoto = tripViewModel::deletePhoto,
                onCaption = tripViewModel::updateCaption,
                onBack = { overlay = Overlay.None },
            )
            return
        }
        Overlay.HighlightDetail -> selectedHighlight?.let { highlight ->
            PlaceDetailScreen(
                stop = null,
                highlight = highlight,
                photoFile = tripViewModel::photoFile,
                onEdit = { overlay = Overlay.EditHighlight },
                onDelete = {
                    tripViewModel.deleteHighlight(highlight)
                    overlay = Overlay.None
                    scope.launch { snackbar.showSnackbar("Highlight deleted") }
                },
                onDeletePhoto = tripViewModel::deletePhoto,
                onCaption = tripViewModel::updateCaption,
                onBack = { overlay = Overlay.None },
            )
            return
        }
        Overlay.None -> Unit
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = tab == WorkspaceTab.Home,
                    onClick = { tab = WorkspaceTab.Home },
                    icon = { Icon(if (tab == WorkspaceTab.Home) Icons.Filled.Home else Icons.Outlined.Home, null) },
                    label = { Text("Home") },
                )
                NavigationBarItem(
                    selected = tab == WorkspaceTab.Map,
                    onClick = { tab = WorkspaceTab.Map },
                    icon = { Icon(if (tab == WorkspaceTab.Map) Icons.Filled.Map else Icons.Outlined.Map, null) },
                    label = { Text("Map") },
                )
                NavigationBarItem(
                    selected = tab == WorkspaceTab.Timeline,
                    onClick = { tab = WorkspaceTab.Timeline },
                    icon = { Icon(if (tab == WorkspaceTab.Timeline) Icons.Filled.Schedule else Icons.Outlined.Schedule, null) },
                    label = { Text("Timeline") },
                )
            }
        },
        floatingActionButton = {
            if (tab != WorkspaceTab.Timeline) {
                Box {
                    if (fabOpen) {
                        androidx.compose.foundation.layout.Column(
                            modifier = Modifier.padding(bottom = 72.dp),
                            horizontalAlignment = Alignment.End,
                        ) {
                            SmallFloatingActionButton(onClick = {
                                fabOpen = false
                                overlay = Overlay.AddHighlight
                            }) {
                                Icon(Icons.Outlined.Star, contentDescription = "Add highlight")
                            }
                            SmallFloatingActionButton(onClick = {
                                fabOpen = false
                                overlay = Overlay.AddStop
                            }) {
                                Icon(Icons.Outlined.AddLocation, contentDescription = "Log stop")
                            }
                        }
                    }
                    FloatingActionButton(onClick = { fabOpen = !fabOpen }) {
                        Icon(if (fabOpen) Icons.Filled.Close else Icons.Filled.Add, contentDescription = "Add")
                    }
                }
            }
        },
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when (tab) {
                WorkspaceTab.Home -> HomeScreen(
                    trip = current,
                    units = units,
                    onBack = onBackToTrips,
                    onSettings = onTripSettings,
                    onOpenStop = { selectedStop = it; overlay = Overlay.StopDetail },
                    onOpenHighlight = { selectedHighlight = it; overlay = Overlay.HighlightDetail },
                    onLogStop = { overlay = Overlay.AddStop },
                )
                WorkspaceTab.Map -> MapScreen(
                    trip = current,
                    onOpenStop = { selectedStop = it; overlay = Overlay.StopDetail },
                    onOpenHighlight = { selectedHighlight = it; overlay = Overlay.HighlightDetail },
                    onDirections = ::openDirections,
                    onMyLocation = { requestLocation {} },
                )
                WorkspaceTab.Timeline -> TimelineScreen(
                    trip = current,
                    onOpenStop = { selectedStop = it; overlay = Overlay.StopDetail },
                    onOpenHighlight = { selectedHighlight = it; overlay = Overlay.HighlightDetail },
                    onLogStop = { overlay = Overlay.AddStop },
                )
            }
        }
    }

    if (showLocationRationale) {
        AlertDialog(
            onDismissRequest = { showLocationRationale = false; pendingLocationAction = null },
            title = { Text("Use your location?") },
            text = { Text("CoastTrip saves a pin for each stop. Location stays on this device.") },
            confirmButton = {
                TextButton(onClick = {
                    showLocationRationale = false
                    locationPermission.launch(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    )
                }) { Text("Continue") }
            },
            dismissButton = {
                TextButton(onClick = { showLocationRationale = false; pendingLocationAction = null }) {
                    Text("Not now")
                }
            },
        )
    }
}
