package com.coasttrip.app.domain

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DistanceTest {
    @Test
    fun routeMiles_emptyOrSinglePoint_isZero() {
        assertThat(Distance.routeMiles(emptyList())).isEqualTo(0.0)
        assertThat(Distance.routeMiles(listOf(LatLngPoint(34.0, -118.0)))).isEqualTo(0.0)
    }

    @Test
    fun santaMonicaToDc_isCoastToCoastScale() {
        val miles = Distance.milesBetween(34.0101, -118.4962, 38.9072, -77.0369)
        assertThat(miles).isGreaterThan(2200.0)
        assertThat(miles).isLessThan(2800.0)
    }

    @Test
    fun format_kilometersConverts() {
        val (value, label) = Distance.format(100.0, DistanceUnit.Kilometers)
        assertThat(label).isEqualTo("Kilometers")
        assertThat(value).isEqualTo(160)
    }

    @Test
    fun defaultUnit_followsUsLocalePreference() {
        assertThat(DistanceUnit.fromStorage(null, localePrefersMiles = true)).isEqualTo(DistanceUnit.Miles)
        assertThat(DistanceUnit.fromStorage(null, localePrefersMiles = false)).isEqualTo(DistanceUnit.Kilometers)
        assertThat(DistanceUnit.fromStorage("km", true)).isEqualTo(DistanceUnit.Kilometers)
    }
}
