package ru.androidlabs.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import ru.androidlabs.R
import ru.androidlabs.data.MusicLibrary
import ru.androidlabs.data.Track
import ru.androidlabs.ui.theme.PlaylistBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(track: Track, library: MusicLibrary, onBack: () -> Unit, onNewPlaylist: () -> Unit) {
    val context = LocalContext.current
    var showPlaylists by rememberSaveable { mutableStateOf(false) }
    var playing by rememberSaveable(track.id) { mutableStateOf(false) }
    var elapsed by rememberSaveable(track.id) { mutableIntStateOf(0) }
    // Звука в моках нет: кнопка показывает работу таймера плеера.
    LaunchedEffect(playing, track.id) {
        while (playing && elapsed < track.seconds) { delay(1000); elapsed++ }
        if (elapsed >= track.seconds) { playing = false; elapsed = 0 }
    }
    DisposableEffect(Unit) { onDispose { playing = false } }
    ScreenLayout("", onBack) {
        Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
            Spacer(Modifier.height(26.dp))
            Image(painterResource(track.cover), "Обложка ${track.title}", Modifier.padding(horizontal = 24.dp).fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
            Spacer(Modifier.height(24.dp))
            Text(track.title, Modifier.padding(horizontal = 24.dp), fontSize = 22.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(12.dp))
            Text(track.artist, Modifier.padding(horizontal = 24.dp), fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(30.dp))
            Row(Modifier.fillMaxWidth().padding(horizontal = 24.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Image(painterResource(R.drawable.ic_queue_button), "Добавить в плейлист", Modifier.size(51.dp).clip(CircleShape).clickable { showPlaylists = true })
                Box(Modifier.size(100.dp).clip(CircleShape).clickable {
                    if (elapsed >= track.seconds) elapsed = 0
                    playing = !playing
                }.semantics { contentDescription = if (playing) "Пауза" else "Воспроизвести" }, contentAlignment = Alignment.Center) {
                    if (playing) Box(Modifier.fillMaxSize().background(Color(0xFF1A1B22)), contentAlignment = Alignment.Center) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            repeat(2) { Box(Modifier.size(8.dp, 32.dp).background(Color.White)) }
                        }
                    }
                    else Image(painterResource(R.drawable.ic_play_button), null, Modifier.size(100.dp))
                }
                val favorite = track.id in library.favoriteIds
                Box(Modifier.size(51.dp).clip(CircleShape).clickable { library.toggleFavorite(track) }.semantics { contentDescription = if (favorite) "Убрать из избранного" else "В избранное" }, contentAlignment = Alignment.Center) {
                    if (favorite) {
                        Box(Modifier.fillMaxSize().background(PlaylistBlue), contentAlignment = Alignment.Center) { Icon(painterResource(R.drawable.ic_favorite), null, Modifier.size(28.dp), tint = Color.White) }
                    } else Image(painterResource(R.drawable.ic_favorite_button), null, Modifier.size(51.dp))
                }
            }
            Text("%d:%02d".format(elapsed / 60, elapsed % 60), Modifier.align(Alignment.CenterHorizontally).padding(top = 4.dp), fontSize = 14.sp)
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth().height(32.dp).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Длительность", Modifier.weight(1f), fontSize = 13.sp, color = Color(0xFFAEAFB4))
                Text(track.duration, fontSize = 13.sp)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
    if (showPlaylists) {
        ModalBottomSheet(
            onDismissRequest = { showPlaylists = false }, containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            dragHandle = { Box(Modifier.padding(top = 8.dp).size(50.dp, 4.dp).background(Color(0xFFE6E8EB), CircleShape)) }
        ) {
            Column(Modifier.fillMaxWidth().heightIn(min = 460.dp)) {
                Text("Добавить в плейлист", Modifier.align(Alignment.CenterHorizontally).padding(top = 26.dp, bottom = 26.dp), fontSize = 19.sp, fontWeight = FontWeight.Medium)
                LazyColumn(Modifier.weight(1f)) {
                    items(library.playlists, key = { it.id }) { playlist ->
                        PlaylistRow(playlist) {
                            val added = library.addTrack(playlist.id, track)
                            Toast.makeText(context, if (added) "Добавлено в ${playlist.name}" else "Трек уже есть в этом плейлисте", Toast.LENGTH_SHORT).show()
                            showPlaylists = false
                        }
                    }
                    item {
                        TextButton(onClick = { showPlaylists = false; onNewPlaylist() }, modifier = Modifier.padding(horizontal = 16.dp)) { Text("Новый плейлист") }
                    }
                }
            }
        }
    }
}
