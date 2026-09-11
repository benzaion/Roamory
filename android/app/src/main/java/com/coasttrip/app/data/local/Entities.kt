package com.coasttrip.app.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey val id: String,
    val title: String,
    val startDateEpoch: Long,
    val endDateEpoch: Long?,
    val notes: String,
)

@Entity(
    tableName = "stops",
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("tripId")],
)
data class StopEntity(
    @PrimaryKey val id: String,
    val tripId: String,
    val latitude: Double,
    val longitude: Double,
    val placeName: String,
    val timestampEpoch: Long,
    val notes: String,
)

@Entity(
    tableName = "highlights",
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("tripId")],
)
data class HighlightEntity(
    @PrimaryKey val id: String,
    val tripId: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val category: String,
    val notes: String,
    val rating: Int,
    val timestampEpoch: Long,
)

@Entity(
    tableName = "photos",
    foreignKeys = [
        ForeignKey(
            entity = StopEntity::class,
            parentColumns = ["id"],
            childColumns = ["stopId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = HighlightEntity::class,
            parentColumns = ["id"],
            childColumns = ["highlightId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("stopId"), Index("highlightId")],
)
data class PhotoEntity(
    @PrimaryKey val id: String,
    val stopId: String?,
    val highlightId: String?,
    val localFileName: String,
    val capturedAtEpoch: Long,
    val caption: String,
)
