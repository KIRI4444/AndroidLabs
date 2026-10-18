package ru.androidlabs.ui

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.androidlabs.data.Playlist
import ru.androidlabs.data.trackCountText

@Composable
fun PlaylistRow(playlist: Playlist, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = 61.dp).clickable(onClick = onClick).padding(horizontal = 13.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        CoverImage(playlist.cover, playlist.coverUri, Modifier.size(45.dp).clip(RoundedCornerShape(2.dp)))
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text(playlist.name, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(trackCountText(playlist.trackIds.size), color = androidx.compose.ui.graphics.Color(0xFFAEAFB4), fontSize = 11.sp)
        }
    }
}

@Composable
fun CoverImage(resource: Int, uri: String?, modifier: Modifier = Modifier, description: String? = null) {
    val context = LocalContext.current
    val bitmap by produceState<ImageBitmap?>(null, uri) {
        value = if (uri == null) null else withContext(Dispatchers.IO) {
            runCatching {
                // Уменьшаем большие фотографии перед загрузкой в память.
                val imageUri = Uri.parse(uri)
                val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                context.contentResolver.openInputStream(imageUri)?.use { BitmapFactory.decodeStream(it, null, bounds) }
                val options = BitmapFactory.Options().apply {
                    while (bounds.outWidth / (inSampleSize * 2) >= 800) inSampleSize *= 2
                }
                context.contentResolver.openInputStream(imageUri)?.use { BitmapFactory.decodeStream(it, null, options)?.asImageBitmap() }
            }.getOrNull()
        }
    }
    val image = bitmap
    if (image == null) Image(painterResource(resource), description, modifier, contentScale = ContentScale.Crop)
    else Image(image, description, modifier, contentScale = ContentScale.Crop)
}
