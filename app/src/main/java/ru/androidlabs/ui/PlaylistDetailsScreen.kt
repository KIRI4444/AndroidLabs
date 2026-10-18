package ru.androidlabs.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.androidlabs.R
import ru.androidlabs.data.MusicLibrary
import ru.androidlabs.data.Playlist
import ru.androidlabs.data.Track
import ru.androidlabs.data.trackCountText
import ru.androidlabs.data.minuteCountText

@Composable
fun PlaylistDetailsScreen(playlist: Playlist, library: MusicLibrary, onBack: () -> Unit, onTrack: (Track) -> Unit) {
    val tracks = library.tracks(playlist)
    val context = LocalContext.current
    var menuExpanded by remember { mutableStateOf(false) }
    ScreenLayout("", onBack) {
        LazyColumn {
            item {
                Box(Modifier.padding(horizontal = 24.dp).fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                    if (playlist.cover == R.drawable.ic_add_photo && playlist.coverUri == null) {
                        Box(Modifier.fillMaxSize().background(Color(0xFFF5F5F5)))
                        Image(painterResource(R.drawable.ic_add_photo), null, Modifier.size(100.dp))
                    } else CoverImage(playlist.cover, playlist.coverUri, Modifier.fillMaxSize(), "Обложка ${playlist.name}")
                }
                Text(playlist.name, Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                if (playlist.description.isNotEmpty()) Text(playlist.description, Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp), fontSize = 18.sp)
                Text("${minuteCountText(tracks.sumOf { it.seconds } / 60)} · ${trackCountText(tracks.size)}", Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp), fontSize = 18.sp)
                Box(Modifier.padding(start = 4.dp)) {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(painterResource(R.drawable.ic_more), "Меню плейлиста", Modifier.size(24.dp).rotate(90f), tint = Color.Unspecified)
                    }
                    DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                        DropdownMenuItem(text = { Text("Поделиться плейлистом") }, onClick = {
                            menuExpanded = false
                            val text = buildString {
                                append(playlist.name)
                                if (playlist.description.isNotEmpty()) append("\n${playlist.description}")
                                append("\n${trackCountText(tracks.size)}")
                                tracks.forEachIndexed { index, track -> append("\n${index + 1}. ${track.artist} — ${track.title} (${track.duration})") }
                            }
                            try {
                                context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_TEXT, text) }, "Поделиться плейлистом"))
                            } catch (_: ActivityNotFoundException) {
                                Toast.makeText(context, "На устройстве нет подходящего приложения", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }
            if (tracks.isEmpty()) item { Text("В этом плейлисте пока нет треков", Modifier.padding(24.dp)) }
            items(tracks, key = { it.id }) { track -> TrackRow(track) { onTrack(track) } }
        }
    }
}
