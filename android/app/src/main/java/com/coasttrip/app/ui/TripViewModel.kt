package com.coasttrip.app.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.coasttrip.app.AppContainer
import com.coasttrip.app.data.TripRepository
import com.coasttrip.app.data.location.DeviceLocation
import com.coasttrip.app.data.location.LocationClient
import com.coasttrip.app.domain.Highlight
import com.coasttrip.app.domain.Stop
import com.coasttrip.app.domain.Trip
import com.coasttrip.app.domain.TripPhoto
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TripViewModel(
    tripId: String,
    private val repository: TripRepository,
    private val locationClient: LocationClient,
) : ViewModel() {
    val trip: StateFlow<Trip?> = repository.observeTrip(tripId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun hasLocationPermission(): Boolean = locationClient.hasPermission()

    suspend fun currentLocation(): DeviceLocation? = locationClient.currentLocation()

    fun saveStop(stop: Stop, photoUris: List<Uri>) = viewModelScope.launch {
        repository.upsertStop(stop, photoUris)
    }

    fun deleteStop(stop: Stop) = viewModelScope.launch { repository.deleteStop(stop) }

    fun saveHighlight(highlight: Highlight, photoUris: List<Uri>) = viewModelScope.launch {
        repository.upsertHighlight(highlight, photoUris)
    }

    fun deleteHighlight(highlight: Highlight) = viewModelScope.launch { repository.deleteHighlight(highlight) }

    fun deletePhoto(photo: TripPhoto) = viewModelScope.launch { repository.deletePhoto(photo) }

    fun updateCaption(photo: TripPhoto, caption: String) = viewModelScope.launch {
        repository.updateCaption(photo, caption)
    }

    fun photoFile(photo: TripPhoto) = repository.photoFile(photo)

    companion object {
        fun factory(container: AppContainer, tripId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TripViewModel(tripId, container.repository, container.locationClient) as T
                }
            }
    }
}
