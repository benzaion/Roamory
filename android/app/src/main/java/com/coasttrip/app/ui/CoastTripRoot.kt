package com.coasttrip.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.coasttrip.app.AppContainer
import com.coasttrip.app.ui.settings.SettingsScreen
import com.coasttrip.app.ui.trips.TripFormScreen
import com.coasttrip.app.ui.trips.TripListScreen
import com.coasttrip.app.ui.workspace.TripWorkspace
import java.util.UUID

@Composable
fun CoastTripRoot(
    container: AppContainer,
    appViewModel: AppViewModel,
) {
    val nav = rememberNavController()
    val trips by appViewModel.trips.collectAsStateWithLifecycle()
    val prefs by appViewModel.prefs.collectAsStateWithLifecycle()
    var restored by remember { mutableStateOf(false) }
    LaunchedEffect(trips, prefs.lastTripId) {
        val id = prefs.lastTripId
        if (!restored && id != null && trips.any { it.id == id }) {
            restored = true
            nav.navigate("trip/$id")
        }
    }

    NavHost(navController = nav, startDestination = "trips") {
        composable("trips") {
            TripListScreen(
                trips = trips,
                units = prefs.units,
                onOpenTrip = { id ->
                    appViewModel.rememberTrip(id)
                    nav.navigate("trip/$id")
                },
                onCreateTrip = { nav.navigate("trip/new") },
                onEditTrip = { id -> nav.navigate("trip/$id/edit") },
                onDeleteTrip = { appViewModel.deleteTrip(it) },
                onSettings = { nav.navigate("settings") },
            )
        }
        composable("trip/new") {
            TripFormScreen(
                initial = null,
                onSave = { trip ->
                    appViewModel.saveTrip(trip) {
                        appViewModel.rememberTrip(trip.id)
                        nav.navigate("trip/${trip.id}") {
                            popUpTo("trips")
                        }
                    }
                },
                onClose = { nav.popBackStack() },
            )
        }
        composable(
            "trip/{tripId}/edit",
            arguments = listOf(navArgument("tripId") { type = NavType.StringType }),
        ) { entry ->
            val id = entry.arguments?.getString("tripId").orEmpty()
            val trip = trips.firstOrNull { it.id == id }
            TripFormScreen(
                initial = trip,
                onSave = { updated ->
                    appViewModel.saveTrip(updated) { nav.popBackStack() }
                },
                onClose = { nav.popBackStack() },
            )
        }
        composable("settings") {
            SettingsScreen(
                prefs = prefs,
                trip = null,
                onUnits = appViewModel::setUnits,
                onTheme = appViewModel::setTheme,
                onDynamicColor = appViewModel::setDynamicColor,
                onSaveTrip = {},
                onBack = { nav.popBackStack() },
            )
        }
        composable(
            "trip/{tripId}",
            arguments = listOf(navArgument("tripId") { type = NavType.StringType }),
        ) { entry ->
            val tripId = entry.arguments?.getString("tripId") ?: return@composable
            val tripViewModel: TripViewModel = viewModel(
                key = tripId,
                factory = TripViewModel.factory(container, tripId),
            )
            TripWorkspace(
                tripViewModel = tripViewModel,
                units = prefs.units,
                onBackToTrips = {
                    appViewModel.rememberTrip(null)
                    nav.popBackStack()
                },
                onTripSettings = { nav.navigate("trip/$tripId/settings") },
            )
        }
        composable(
            "trip/{tripId}/settings",
            arguments = listOf(navArgument("tripId") { type = NavType.StringType }),
        ) { entry ->
            val id = entry.arguments?.getString("tripId").orEmpty()
            val trip = trips.firstOrNull { it.id == id }
            SettingsScreen(
                prefs = prefs,
                trip = trip,
                onUnits = appViewModel::setUnits,
                onTheme = appViewModel::setTheme,
                onDynamicColor = appViewModel::setDynamicColor,
                onSaveTrip = { appViewModel.saveTrip(it) },
                onBack = { nav.popBackStack() },
            )
        }
    }
}

internal fun newId(): String = UUID.randomUUID().toString()
