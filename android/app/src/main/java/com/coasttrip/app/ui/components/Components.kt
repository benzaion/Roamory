package com.coasttrip.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.coasttrip.app.domain.HighlightCategory
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

val DateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
val DateTimeFormatterShort: DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)

fun formatDate(epoch: Long): String =
    DateFormatter.format(Instant.ofEpochMilli(epoch).atZone(ZoneId.systemDefault()))

fun formatDateTime(epoch: Long): String =
    DateTimeFormatterShort.format(Instant.ofEpochMilli(epoch).atZone(ZoneId.systemDefault()))

fun formatCoord(lat: Double, lng: Double): String = String.format("%.4f, %.4f", lat, lng)

fun HighlightCategory.iconName(): String = when (this) {
    HighlightCategory.Scenic -> "park"
    HighlightCategory.Food -> "restaurant"
    HighlightCategory.Lodging -> "hotel"
    HighlightCategory.Landmark -> "star"
    HighlightCategory.Other -> "place"
}

@Composable
fun EmptyState(
    title: String,
    message: String,
    action: String,
    onAction: () -> Unit,
    icon: ImageVector = Icons.Outlined.Map,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(12.dp))
        Text(title, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text(message, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onAction) { Text(action) }
    }
}
