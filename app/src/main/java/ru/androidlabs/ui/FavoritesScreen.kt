package ru.androidlabs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.androidlabs.data.MusicLibrary
import ru.androidlabs.data.Track
import ru.androidlabs.data.mockTracks

@Composable
fun FavoritesScreen(library: MusicLibrary, onTrack: (Track) -> Unit) {
    val tracks = library.favoriteIds.mapNotNull { id -> mockTracks.find { it.id == id } }
    Column(Modifier.fillMaxSize().background(Color.White).safeDrawingPadding()) {
        ScreenHeader("Избранное")
        if (tracks.isEmpty()) EmptyState("Ваша медиатека пуста", modifier = Modifier.padding(top = 154.dp))
        else LazyColumn(contentPadding = PaddingValues(top = 4.dp)) {
            items(tracks, key = { it.id }) { track -> TrackRow(track) { onTrack(track) } }
        }
    }
}
