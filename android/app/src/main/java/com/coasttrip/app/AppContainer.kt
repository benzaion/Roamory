package com.coasttrip.app

import android.content.Context
import com.coasttrip.app.data.TripRepository
import com.coasttrip.app.data.local.CoastTripDatabase
import com.coasttrip.app.data.location.LocationClient
import com.coasttrip.app.data.photos.PhotoStorage
import com.coasttrip.app.data.prefs.UserPreferences

class AppContainer(context: Context) {
    private val appContext = context.applicationContext
    val database = CoastTripDatabase.create(appContext)
    val photoStorage = PhotoStorage(appContext)
    val repository = TripRepository(database.dao(), photoStorage)
    val preferences = UserPreferences(appContext)
    val locationClient = LocationClient(appContext)
}
