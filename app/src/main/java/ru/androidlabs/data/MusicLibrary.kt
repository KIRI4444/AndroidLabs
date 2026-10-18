package ru.androidlabs.data

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import org.json.JSONArray
import org.json.JSONObject
import ru.androidlabs.R

data class Playlist(
    val id: Int,
    val name: String,
    val description: String = "",
    val cover: Int = R.drawable.ic_add_photo,
    val coverUri: String? = null,
    val trackIds: List<Int> = emptyList()
)

// Локальные данные вместо сервера. Изменения сохраняются между запусками.
class MusicLibrary(context: Context) {
    private val preferences = context.getSharedPreferences("music_library", Context.MODE_PRIVATE)
    val favoriteIds = mutableStateListOf<Int>()
    val playlists = mutableStateListOf<Playlist>()

    init {
        val favorites = preferences.getString("favorites", "[]")
        runCatching {
            val array = JSONArray(favorites)
            for (i in 0 until array.length()) favoriteIds.add(array.getInt(i))
        }
        val saved = preferences.getString("playlists", null)
        if (saved == null) {
            playlists.addAll(listOf(
                Playlist(1, "Best songs 2021", "2022", R.drawable.playlist_best, trackIds = listOf(1, 2, 3, 4)),
                Playlist(2, "Summer Party", "Для хорошего настроения", R.drawable.playlist_summer, trackIds = listOf(5, 6, 8)),
                Playlist(3, "Morning")
            ))
        } else runCatching {
            val array = JSONArray(saved)
            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)
                val ids = item.getJSONArray("tracks")
                playlists.add(Playlist(
                    id = item.getInt("id"), name = item.getString("name"),
                    description = item.optString("description"), cover = coverFor(item.optString("cover")),
                    coverUri = item.optString("uri").takeIf { it.isNotEmpty() },
                    trackIds = List(ids.length()) { ids.getInt(it) }
                ))
            }
        }
    }

    fun toggleFavorite(track: Track) {
        if (track.id in favoriteIds) favoriteIds.remove(track.id) else favoriteIds.add(0, track.id)
        save()
    }

    fun createPlaylist(name: String, description: String, coverUri: String?) {
        val title = name.trim()
        if (title.isEmpty()) return
        playlists.add(Playlist((playlists.maxOfOrNull { it.id } ?: 0) + 1, title, description.trim(), coverUri = coverUri))
        save()
    }

    fun addTrack(playlistId: Int, track: Track): Boolean {
        val index = playlists.indexOfFirst { it.id == playlistId }
        if (index == -1 || track.id in playlists[index].trackIds) return false
        playlists[index] = playlists[index].copy(trackIds = playlists[index].trackIds + track.id)
        save()
        return true
    }

    fun tracks(playlist: Playlist): List<Track> = playlist.trackIds.mapNotNull { id -> mockTracks.find { it.id == id } }

    private fun save() {
        val array = JSONArray()
        playlists.forEach { playlist ->
            array.put(JSONObject().apply {
                put("id", playlist.id); put("name", playlist.name); put("description", playlist.description)
                put("cover", when (playlist.cover) { R.drawable.playlist_best -> "best"; R.drawable.playlist_summer -> "summer"; else -> "placeholder" })
                put("uri", playlist.coverUri ?: ""); put("tracks", JSONArray(playlist.trackIds))
            })
        }
        preferences.edit().putString("favorites", JSONArray(favoriteIds.toList()).toString()).putString("playlists", array.toString()).apply()
    }

    private fun coverFor(key: String): Int = when (key) {
        "best" -> R.drawable.playlist_best
        "summer" -> R.drawable.playlist_summer
        else -> R.drawable.ic_add_photo
    }
}

fun trackCountText(count: Int): String {
    val word = when {
        count % 100 in 11..14 -> "треков"
        count % 10 == 1 -> "трек"
        count % 10 in 2..4 -> "трека"
        else -> "треков"
    }
    return "$count $word"
}
