package com.coasttrip.app.domain

import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object Distance {
    private const val EARTH_RADIUS_MILES = 3958.8
    const val MILES_TO_KM = 1.60934
    const val METERS_PER_MILE = 1609.344

    fun milesBetween(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double,
    ): Double = haversineMiles(lat1, lon1, lat2, lon2)

    fun routeMiles(points: List<LatLngPoint>): Double {
        if (points.size < 2) return 0.0
        return points.zipWithNext { a, b -> milesBetween(a.latitude, a.longitude, b.latitude, b.longitude) }.sum()
    }

    fun format(miles: Double, unit: DistanceUnit): Pair<Long, String> {
        val value = if (unit == DistanceUnit.Kilometers) miles * MILES_TO_KM else miles
        return value.toLong() to unit.label
    }

    internal fun haversineMiles(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double,
    ): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val rLat1 = Math.toRadians(lat1)
        val rLat2 = Math.toRadians(lat2)
        val h = sin(dLat / 2).pow(2) + cos(rLat1) * cos(rLat2) * sin(dLon / 2).pow(2)
        return 2 * EARTH_RADIUS_MILES * asin(sqrt(h))
    }
}

data class LatLngPoint(val latitude: Double, val longitude: Double)

enum class DistanceUnit(val storage: String, val label: String) {
    Miles("mi", "Miles"),
    Kilometers("km", "Kilometers");

    companion object {
        fun fromStorage(value: String?, localePrefersMiles: Boolean): DistanceUnit {
            return when (value) {
                Miles.storage -> Miles
                Kilometers.storage -> Kilometers
                else -> if (localePrefersMiles) Miles else Kilometers
            }
        }
    }
}
