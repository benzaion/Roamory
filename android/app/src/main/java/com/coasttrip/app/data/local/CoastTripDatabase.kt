package com.coasttrip.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TripEntity::class, StopEntity::class, HighlightEntity::class, PhotoEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class CoastTripDatabase : RoomDatabase() {
    abstract fun dao(): CoastTripDao

    companion object {
        fun create(context: Context): CoastTripDatabase =
            Room.databaseBuilder(context, CoastTripDatabase::class.java, "coasttrip.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}
