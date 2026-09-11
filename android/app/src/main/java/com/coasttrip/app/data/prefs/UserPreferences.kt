package com.coasttrip.app.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.coasttrip.app.domain.DistanceUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale

private val Context.dataStore by preferencesDataStore("coasttrip_prefs")

enum class ThemeMode { System, Light, Dark }

data class AppPreferences(
    val units: DistanceUnit,
    val theme: ThemeMode,
    val dynamicColor: Boolean,
    val lastTripId: String?,
)

class UserPreferences(private val context: Context) {
    private val unitsKey = stringPreferencesKey("units")
    private val themeKey = stringPreferencesKey("theme")
    private val dynamicKey = booleanPreferencesKey("dynamic_color")
    private val lastTripKey = stringPreferencesKey("last_trip_id")

    val preferences: Flow<AppPreferences> = context.dataStore.data.map { prefs ->
        val localePrefersMiles = Locale.getDefault().country.equals("US", ignoreCase = true)
        AppPreferences(
            units = DistanceUnit.fromStorage(prefs[unitsKey], localePrefersMiles),
            theme = ThemeMode.entries.firstOrNull { it.name == prefs[themeKey] } ?: ThemeMode.System,
            dynamicColor = prefs[dynamicKey] ?: true,
            lastTripId = prefs[lastTripKey],
        )
    }

    suspend fun setUnits(unit: DistanceUnit) {
        context.dataStore.edit { it[unitsKey] = unit.storage }
    }

    suspend fun setTheme(mode: ThemeMode) {
        context.dataStore.edit { it[themeKey] = mode.name }
    }

    suspend fun setDynamicColor(enabled: Boolean) {
        context.dataStore.edit { it[dynamicKey] = enabled }
    }

    suspend fun setLastTripId(tripId: String?) {
        context.dataStore.edit {
            if (tripId == null) it.remove(lastTripKey) else it[lastTripKey] = tripId
        }
    }
}
