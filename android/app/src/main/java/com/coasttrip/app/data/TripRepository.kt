package com.coasttrip.app.data

import android.net.Uri
import com.coasttrip.app.data.local.CoastTripDao
import com.coasttrip.app.data.local.HighlightEntity
import com.coasttrip.app.data.local.PhotoEntity
import com.coasttrip.app.data.local.StopEntity
import com.coasttrip.app.data.local.TripEntity
import com.coasttrip.app.data.photos.PhotoStorage
import com.coasttrip.app.domain.Highlight
import com.coasttrip.app.domain.HighlightCategory
import com.coasttrip.app.domain.Stop
import com.coasttrip.app.domain.Trip
import com.coasttrip.app.domain.TripPhoto
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TripRepository(
    private val dao: CoastTripDao,
    private val photos: PhotoStorage,
) {
    fun observeTripSummaries(): Flow<List<Trip>> {
        return combine(dao.observeTrips(), dao.observeAllPhotos(), dao.observeAllStops(), dao.observeAllHighlights()) { trips, allPhotos, allStops, allHighlights ->
            trips.map { trip ->
                val stops = allStops.filter { it.tripId == trip.id }
                val highlights = allHighlights.filter { it.tripId == trip.id }
                trip.toDomain(
                    stops = stops.map { stop ->
                        stop.toDomain(allPhotos.filter { it.stopId == stop.id }.map { it.toDomain() })
                    },
                    highlights = highlights.map { highlight ->
                        highlight.toDomain(allPhotos.filter { it.highlightId == highlight.id }.map { it.toDomain() })
                    },
                )
            }
        }
    }

    fun observeTrip(tripId: String): Flow<Trip?> {
        return observeTripSummaries().map { list -> list.firstOrNull { it.id == tripId } }
    }

    suspend fun getTrip(id: String): Trip? = observeTrip(id).first()

    suspend fun upsertTrip(trip: Trip) {
        dao.upsertTrip(trip.toEntity())
    }

    suspend fun deleteTrip(tripId: String) {
        val trip = getTrip(tripId)
        trip?.stops?.flatMap { it.photos }?.forEach { photos.delete(it.localFileName) }
        trip?.highlights?.flatMap { it.photos }?.forEach { photos.delete(it.localFileName) }
        dao.deleteTrip(tripId)
    }

    suspend fun upsertStop(stop: Stop, newPhotoUris: List<Uri> = emptyList()) {
        dao.upsertStop(stop.toEntity())
        attachPhotos(newPhotoUris, stopId = stop.id, highlightId = null)
    }

    suspend fun deleteStop(stop: Stop) {
        stop.photos.forEach { photos.delete(it.localFileName) }
        dao.deleteStop(stop.id)
    }

    suspend fun upsertHighlight(highlight: Highlight, newPhotoUris: List<Uri> = emptyList()) {
        dao.upsertHighlight(highlight.toEntity())
        attachPhotos(newPhotoUris, stopId = null, highlightId = highlight.id)
    }

    suspend fun deleteHighlight(highlight: Highlight) {
        highlight.photos.forEach { photos.delete(it.localFileName) }
        dao.deleteHighlight(highlight.id)
    }

    suspend fun deletePhoto(photo: TripPhoto) {
        photos.delete(photo.localFileName)
        dao.deletePhoto(photo.id)
    }

    suspend fun updateCaption(photo: TripPhoto, caption: String) {
        dao.upsertPhoto(
            PhotoEntity(
                id = photo.id,
                stopId = photo.stopId,
                highlightId = photo.highlightId,
                localFileName = photo.localFileName,
                capturedAtEpoch = photo.capturedAtEpoch,
                caption = caption,
            ),
        )
    }

    fun photoFile(photo: TripPhoto) = photos.fileFor(photo.localFileName)

    private suspend fun attachPhotos(uris: List<Uri>, stopId: String?, highlightId: String?) {
        uris.forEach { uri ->
            val fileName = photos.saveFromUri(uri)
            dao.upsertPhoto(
                PhotoEntity(
                    id = UUID.randomUUID().toString(),
                    stopId = stopId,
                    highlightId = highlightId,
                    localFileName = fileName,
                    capturedAtEpoch = System.currentTimeMillis(),
                    caption = "",
                ),
            )
        }
    }
}

private fun TripEntity.toDomain(stops: List<Stop> = emptyList(), highlights: List<Highlight> = emptyList()) =
    Trip(id, title, startDateEpoch, endDateEpoch, notes, stops, highlights)

private fun StopEntity.toDomain(photos: List<TripPhoto>) =
    Stop(id, tripId, latitude, longitude, placeName, timestampEpoch, notes, photos)

private fun HighlightEntity.toDomain(photos: List<TripPhoto>) =
    Highlight(id, tripId, name, latitude, longitude, HighlightCategory.fromStorage(category), notes, rating, timestampEpoch, photos)

private fun PhotoEntity.toDomain() =
    TripPhoto(id, stopId, highlightId, localFileName, capturedAtEpoch, caption)

private fun Trip.toEntity() = TripEntity(id, title, startDateEpoch, endDateEpoch, notes)
private fun Stop.toEntity() = StopEntity(id, tripId, latitude, longitude, placeName, timestampEpoch, notes)
private fun Highlight.toEntity() =
    HighlightEntity(id, tripId, name, latitude, longitude, category.storage, notes, rating, timestampEpoch)
