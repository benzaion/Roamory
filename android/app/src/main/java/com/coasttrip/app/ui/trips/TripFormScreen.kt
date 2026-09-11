package com.coasttrip.app.ui.trips

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coasttrip.app.domain.Trip
import com.coasttrip.app.ui.components.formatDate
import com.coasttrip.app.ui.newId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripFormScreen(
    initial: Trip?,
    onSave: (Trip) -> Unit,
    onClose: () -> Unit,
) {
    var title by remember { mutableStateOf(initial?.title.orEmpty()) }
    var notes by remember { mutableStateOf(initial?.notes.orEmpty()) }
    var start by remember { mutableStateOf(initial?.startDateEpoch ?: System.currentTimeMillis()) }
    var end by remember { mutableStateOf(initial?.endDateEpoch) }
    var pickingStart by remember { mutableStateOf(false) }
    var pickingEnd by remember { mutableStateOf(false) }
    val canSave = title.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (initial == null) "Start a trip" else "Edit trip") },
                navigationIcon = {
                    IconButton(onClick = onClose) { Icon(Icons.Filled.Close, contentDescription = "Close") }
                },
                actions = {
                    TextButton(enabled = canSave, onClick = {
                        onSave(
                            Trip(
                                id = initial?.id ?: newId(),
                                title = title.trim(),
                                startDateEpoch = start,
                                endDateEpoch = end,
                                notes = notes.trim(),
                                stops = initial?.stops.orEmpty(),
                                highlights = initial?.highlights.orEmpty(),
                            ),
                        )
                    }) { Text("Save") }
                },
            )
        },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Trip name") },
            )
            TextButton(onClick = { pickingStart = true }) { Text("Start date: ${formatDate(start)}") }
            TextButton(onClick = { pickingEnd = true }) {
                Text(end?.let { "End date: ${formatDate(it)}" } ?: "End date: optional")
            }
            if (end != null) {
                TextButton(onClick = { end = null }) { Text("Clear end date") }
            }
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Notes") },
                minLines = 3,
            )
        }
    }

    if (pickingStart || pickingEnd) {
        val state = rememberDatePickerState(initialSelectedDateMillis = if (pickingStart) start else end)
        DatePickerDialog(
            onDismissRequest = { pickingStart = false; pickingEnd = false },
            confirmButton = {
                TextButton(onClick = {
                    val value = state.selectedDateMillis
                    if (value != null) {
                        if (pickingStart) start = value else end = value
                    }
                    pickingStart = false
                    pickingEnd = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { pickingStart = false; pickingEnd = false }) { Text("Cancel") }
            },
        ) {
            DatePicker(state = state)
        }
    }
}
