package ru.androidlabs.data

import androidx.annotation.DrawableRes
import ru.androidlabs.R

data class Track(val id: Int, val title: String, val artist: String, val seconds: Int, @param:DrawableRes val cover: Int) {
    val duration: String get() = "%d:%02d".format(seconds / 60, seconds % 60)
}

val mockTracks = listOf(
    Track(1, "Yesterday (Remastered 2009)", "The Beatles", 175, R.drawable.album_yesterday),
    Track(2, "Here Comes The Sun (Remastered 2009)", "The Beatles", 241, R.drawable.album_sun),
    Track(3, "No Reply", "The Beatles", 312, R.drawable.ic_track_placeholder),
    Track(4, "Let It Be", "The Beatles", 361, R.drawable.album_let_it_be),
    Track(5, "Girl", "The Beatles", 251, R.drawable.album_let_it_be),
    Track(6, "Michelle", "The Beatles", 181, R.drawable.album_let_it_be),
    Track(7, "Eleanor Rigby", "The Beatles", 372, R.drawable.album_let_it_be),
    Track(8, "Come Together", "The Beatles", 249, R.drawable.album_let_it_be),
    Track(9, "Bohemian Rhapsody", "Queen", 355, R.drawable.ic_track_placeholder),
    Track(10, "Sonne", "Rammstein", 272, R.drawable.ic_track_placeholder),
    Track(11, "Just the Way You Are", "Bruno Mars", 220, R.drawable.ic_track_placeholder)
)

fun findTracks(query: String): List<Track> = mockTracks.filter {
    it.title.contains(query.trim(), ignoreCase = true) || it.artist.contains(query.trim(), ignoreCase = true)
}
