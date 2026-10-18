package ru.androidlabs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.androidlabs.R
import ru.androidlabs.data.MusicLibrary
import ru.androidlabs.data.Playlist

@Composable
fun PlaylistsScreen(library: MusicLibrary, onBack: () -> Unit, onNewPlaylist: () -> Unit, onPlaylist: (Playlist) -> Unit) {
    ScreenLayout("Плейлисты", onBack) {
        Box(Modifier.fillMaxWidth().weight(1f)) {
            if (library.playlists.isEmpty()) {
                EmptyState("Вы ещё не создали ни одного плейлиста", modifier = Modifier.padding(top = 154.dp))
            } else LazyColumn(contentPadding = PaddingValues(bottom = 100.dp)) {
                items(library.playlists, key = { it.id }) { playlist -> PlaylistRow(playlist) { onPlaylist(playlist) } }
            }
            Box(
                Modifier.align(Alignment.BottomEnd).padding(end = 17.dp, bottom = 32.dp).size(51.dp).clip(CircleShape)
                    .background(Color(0x401A1B22)).clickable(onClick = onNewPlaylist).semantics { contentDescription = "Новый плейлист" },
                contentAlignment = Alignment.Center
            ) {
                Icon(painterResource(R.drawable.ic_add), null, Modifier.size(23.604.dp), tint = Color.Unspecified)
            }
        }
    }
}
