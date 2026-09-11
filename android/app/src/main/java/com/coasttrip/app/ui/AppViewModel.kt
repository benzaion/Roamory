package com.coasttrip.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.coasttrip.app.AppContainer
import com.coasttrip.app.data.TripRepository
import com.coasttrip.app.data.prefs.AppPreferences
import com.coasttrip.app.data.prefs.ThemeMode
import com.coasttrip.app.data.prefs.UserPreferences
import com.coasttrip.app.domain.DistanceUnit
import com.coasttrip.app.domain.Trip
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    private val repository: TripRepository,
    private val preferences: UserPreferences,
) : ViewModel() {
    val trips: StateFlow<List<Trip>> = repository.observeTripSummaries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val prefs: StateFlow<AppPreferences> = preferences.preferences
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            AppPreferences(DistanceUnit.Miles, ThemeMode.System, true, null),
        )

    fun setUnits(unit: DistanceUnit) = viewModelScope.launch { preferences.setUnits(unit) }
    fun setTheme(mode: ThemeMode) = viewModelScope.launch { preferences.setTheme(mode) }
    fun setDynamicColor(enabled: Boolean) = viewModelScope.launch { preferences.setDynamicColor(enabled) }
    fun rememberTrip(tripId: String?) = viewModelScope.launch { preferences.setLastTripId(tripId) }

    fun saveTrip(trip: Trip, onDone: () -> Unit = {}) = viewModelScope.launch {
        repository.upsertTrip(trip)
        onDone()
    }
    fun deleteTrip(tripId: String) = viewModelScope.launch {
        repository.deleteTrip(tripId)
        if (prefs.value.lastTripId == tripId) preferences.setLastTripId(null)
    }

    companion object {
        fun factory(container: AppContainer): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AppViewModel(container.repository, container.preferences) as T
                }
            }
    }
}
