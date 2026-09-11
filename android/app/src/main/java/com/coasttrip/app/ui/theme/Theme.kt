package com.coasttrip.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.coasttrip.app.data.prefs.ThemeMode

private val Seed = Color(0xFF3A72FB)
private val LightPrimary = Color(0xFF3A72FB)
private val DarkPrimary = Color(0xFFB4C5FF)

private val LightColors = lightColorScheme(
    primary = LightPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCE4FF),
    onPrimaryContainer = Color(0xFF00174B),
    secondary = Color(0xFF3A72FB),
    surface = Color(0xFFF7F8FD),
    onSurface = Color(0xFF1A1C20),
    surfaceContainer = Color(0xFFEBEDF6),
    surfaceContainerHigh = Color(0xFFE4E6F0),
    error = Color(0xFFBA1A1A),
)

private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = Color(0xFF002B75),
    primaryContainer = Color(0xFF2548C7),
    onPrimaryContainer = Color(0xFFDCE4FF),
    surface = Color(0xFF111318),
    onSurface = Color(0xFFE3E2E8),
    surfaceContainer = Color(0xFF1D2026),
    surfaceContainerHigh = Color(0xFF272A31),
    error = Color(0xFFFFB4AB),
)

@Composable
fun CoastTripTheme(
    themeMode: ThemeMode,
    dynamicColor: Boolean,
    content: @Composable () -> Unit,
) {
    val dark = when (themeMode) {
        ThemeMode.System -> isSystemInDarkTheme()
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }
    val context = LocalContext.current
    val colors = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (dark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        if (dark) DarkColors else LightColors
    }
    MaterialTheme(colorScheme = colors.copy(primary = if (dynamicColor) colors.primary else Seed), content = content)
}
