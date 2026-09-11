package com.coasttrip.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coasttrip.app.ui.AppViewModel
import com.coasttrip.app.ui.CoastTripRoot
import com.coasttrip.app.ui.theme.CoastTripTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val container = (application as CoastTripApplication).container
        setContent {
            val appViewModel: AppViewModel = viewModel(factory = AppViewModel.factory(container))
            val prefs by appViewModel.prefs.collectAsStateWithLifecycle()
            CoastTripTheme(themeMode = prefs.theme, dynamicColor = prefs.dynamicColor) {
                CoastTripRoot(container = container, appViewModel = appViewModel)
            }
        }
    }
}
