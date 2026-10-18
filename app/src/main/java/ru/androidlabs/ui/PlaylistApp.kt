package ru.androidlabs.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import ru.androidlabs.R

@Composable
fun PlaylistApp() {
    var screen by rememberSaveable { mutableStateOf(R.string.app_name) }
    val goBack = { screen = R.string.app_name }
    BackHandler(enabled = screen != R.string.app_name, onBack = goBack)
    when (screen) {
        R.string.app_name -> MainScreen { screen = it }
        R.string.settings -> SettingsScreen(goBack)
        R.string.search -> SearchScreen(goBack, onTrack = {})
        else -> ScreenLayout(androidx.compose.ui.res.stringResource(screen), goBack) { }
    }
}
