package ru.androidlabs.ui

import android.graphics.Color as AndroidColor
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.platform.LocalContext
import ru.androidlabs.R
import ru.androidlabs.data.MusicLibrary
import ru.androidlabs.data.Track
import ru.androidlabs.data.mockTracks

@Composable
fun PlaylistApp() {
    val context = LocalContext.current
    val library = remember { MusicLibrary(context.applicationContext) }
    val screenStates = rememberSaveableStateHolder()
    var screen by rememberSaveable { mutableStateOf("home") }
    var playerOrigin by rememberSaveable { mutableStateOf("search") }
    var selectedTrackId by rememberSaveable { mutableIntStateOf(1) }
    var selectedPlaylistId by rememberSaveable { mutableIntStateOf(1) }
    var createOrigin by rememberSaveable { mutableStateOf("playlists") }
    val goBack: () -> Unit = {
        screen = when (screen) { "player" -> playerOrigin; "new_playlist" -> createOrigin; "playlist" -> "playlists"; else -> "home" }
    }
    val newPlaylist: () -> Unit = { createOrigin = screen; screen = "new_playlist" }
    val openTrack: (Track) -> Unit = { track -> playerOrigin = screen; selectedTrackId = track.id; screen = "player" }
    BackHandler(enabled = screen != "home", onBack = goBack)
    SideEffect {
        (context as? ComponentActivity)?.enableEdgeToEdge(
            statusBarStyle = if (screen == "home") SystemBarStyle.dark(AndroidColor.TRANSPARENT) else SystemBarStyle.light(AndroidColor.TRANSPARENT, AndroidColor.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(AndroidColor.TRANSPARENT, AndroidColor.TRANSPARENT)
        )
    }
    screenStates.SaveableStateProvider(screen) {
        when (screen) {
            "home" -> MainScreen { section ->
                screen = when (section) {
                    R.string.search -> "search"
                    R.string.settings -> "settings"
                    R.string.favorites -> "favorites"
                    else -> "playlists"
                }
            }
            "settings" -> SettingsScreen(goBack)
            "search" -> SearchScreen(goBack, openTrack)
            "favorites" -> FavoritesScreen(library, openTrack)
            "player" -> PlayerScreen(mockTracks.first { it.id == selectedTrackId }, library, goBack, onNewPlaylist = newPlaylist)
            "playlists" -> PlaylistsScreen(library, goBack, newPlaylist) { playlist -> selectedPlaylistId = playlist.id; screen = "playlist" }
            "new_playlist" -> NewPlaylistScreen(library, goBack) {
                screenStates.removeState("new_playlist")
                screen = createOrigin
            }
            "playlist" -> library.playlists.find { it.id == selectedPlaylistId }?.let { playlist ->
                PlaylistDetailsScreen(playlist, library, goBack, openTrack)
            }
        }
    }
}
