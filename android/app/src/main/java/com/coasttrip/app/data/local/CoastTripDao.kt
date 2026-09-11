package com.coasttrip.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CoastTripDao {
    @Query("SELECT * FROM trips ORDER BY startDateEpoch DESC")
    fun observeTrips(): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE id = :id")
    fun observeTrip(id: String): Flow<TripEntity?>

    @Query("SELECT * FROM trips WHERE id = :id")
    suspend fun getTrip(id: String): TripEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTrip(trip: TripEntity)

    @Update
    suspend fun updateTrip(trip: TripEntity)

    @Query("DELETE FROM trips WHERE id = :id")
    suspend fun deleteTrip(id: String)

    @Query("SELECT * FROM stops ORDER BY timestampEpoch ASC")
    fun observeAllStops(): Flow<List<StopEntity>>

    @Query("SELECT * FROM stops WHERE tripId = :tripId ORDER BY timestampEpoch ASC")
    fun observeStops(tripId: String): Flow<List<StopEntity>>

    @Query("SELECT * FROM stops WHERE id = :id")
    suspend fun getStop(id: String): StopEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStop(stop: StopEntity)

    @Query("DELETE FROM stops WHERE id = :id")
    suspend fun deleteStop(id: String)

    @Query("SELECT * FROM highlights ORDER BY timestampEpoch ASC")
    fun observeAllHighlights(): Flow<List<HighlightEntity>>

    @Query("SELECT * FROM highlights WHERE tripId = :tripId ORDER BY timestampEpoch ASC")
    fun observeHighlights(tripId: String): Flow<List<HighlightEntity>>

    @Query("SELECT * FROM highlights WHERE id = :id")
    suspend fun getHighlight(id: String): HighlightEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHighlight(highlight: HighlightEntity)

    @Query("DELETE FROM highlights WHERE id = :id")
    suspend fun deleteHighlight(id: String)

    @Query("SELECT * FROM photos WHERE stopId = :stopId")
    fun observeStopPhotos(stopId: String): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM photos WHERE highlightId = :highlightId")
    fun observeHighlightPhotos(highlightId: String): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM photos WHERE stopId = :stopId OR highlightId = :highlightId")
    suspend fun photosFor(stopId: String?, highlightId: String?): List<PhotoEntity>

    @Query("SELECT * FROM photos WHERE id = :id")
    suspend fun getPhoto(id: String): PhotoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPhoto(photo: PhotoEntity)

    @Query("DELETE FROM photos WHERE id = :id")
    suspend fun deletePhoto(id: String)

    @Query("SELECT * FROM photos")
    fun observeAllPhotos(): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM photos WHERE stopId IN (:stopIds) OR highlightId IN (:highlightIds)")
    suspend fun photosForParents(stopIds: List<String>, highlightIds: List<String>): List<PhotoEntity>

    @Transaction
    suspend fun replaceStopPhotos(stopId: String, photos: List<PhotoEntity>) {
        // individual deletes happen in repository so files can be removed
        photos.forEach { upsertPhoto(it) }
    }
}
