package com.example.dragsample.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import com.example.dragsample.BottomNavPortrait
import com.example.dragsample.BottomNavUiActions
import com.example.dragsample.BottomNavUiState

@Composable
fun BottomNavMenu(uiState: BottomNavUiState, uiActions: BottomNavUiActions) {
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            //BottomNavLandscape(uiState, uiActions)
        }
        else -> {
            BottomNavPortrait(uiState, uiActions)
        }
    }
}