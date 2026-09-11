package com.coasttrip.app.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import android.Manifest

data class DeviceLocation(val latitude: Double, val longitude: Double)

class LocationClient(private val context: Context) {
    private val client = LocationServices.getFusedLocationProviderClient(context)

    fun hasPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    suspend fun currentLocation(): DeviceLocation? {
        if (!hasPermission()) return null
        return suspendCancellableCoroutine { cont ->
            val token = CancellationTokenSource()
            cont.invokeOnCancellation { token.cancel() }
            client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, token.token)
                .addOnSuccessListener { location ->
                    if (location == null) {
                        cont.resume(null)
                    } else {
                        cont.resume(DeviceLocation(location.latitude, location.longitude))
                    }
                }
                .addOnFailureListener { cont.resume(null) }
        }
    }
}
