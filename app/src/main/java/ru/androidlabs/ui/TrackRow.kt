package ru.androidlabs.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.androidlabs.R
import ru.androidlabs.data.Track

@Composable
fun TrackRow(track: Track, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = 61.dp).clickable(onClick = onClick).padding(horizontal = 13.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(painterResource(track.cover), null, Modifier.size(45.dp).clip(RoundedCornerShape(2.dp)), contentScale = ContentScale.Crop)
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text(track.title, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(track.artist, Modifier.weight(1f, fill = false), color = Color(0xFFAEAFB4), fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Icon(painterResource(R.drawable.ic_dot), null, Modifier.size(13.dp), tint = Color.Unspecified)
                Text(track.duration, color = Color(0xFFAEAFB4), fontSize = 11.sp)
            }
        }
        Spacer(Modifier.width(8.dp))
        Icon(painterResource(R.drawable.ic_forward), null, Modifier.size(24.dp), tint = Color.Unspecified)
    }
}
