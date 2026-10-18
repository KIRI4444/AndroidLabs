package ru.androidlabs.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import ru.androidlabs.R
import ru.androidlabs.data.MusicLibrary
import ru.androidlabs.ui.theme.PlaylistBlue

@Composable
fun NewPlaylistScreen(library: MusicLibrary, onBack: () -> Unit, onCreated: () -> Unit) {
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var coverUri by rememberSaveable { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val keyboard = LocalSoftwareKeyboardController.current
    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null) {
            runCatching { context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION) }
            coverUri = uri.toString()
        }
    }
    val fieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = Color(0xFFAEAFB4), focusedBorderColor = PlaylistBlue,
        unfocusedLabelColor = Color(0xFF1A1B22), focusedLabelColor = PlaylistBlue
    )
    ScreenLayout("Новый плейлист", onBack) {
        Column(Modifier.fillMaxWidth().weight(1f).imePadding()) {
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                Spacer(Modifier.height(26.dp))
                Box(
                    Modifier.padding(horizontal = 24.dp).fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(8.dp))
                        .clickable { photoPicker.launch(arrayOf("image/*")) }.semantics { contentDescription = "Выбрать обложку" },
                    contentAlignment = Alignment.Center
                ) {
                    if (coverUri == null) Image(painterResource(R.drawable.ic_add_photo), null, Modifier.size(100.dp))
                    else CoverImage(R.drawable.ic_add_photo, coverUri, Modifier.fillMaxSize())
                }
                Spacer(Modifier.height(32.dp))
                OutlinedTextField(
                    value = name, onValueChange = { name = it }, label = { Text("Название*") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    shape = RoundedCornerShape(4.dp), colors = fieldColors
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = description, onValueChange = { description = it }, label = { Text("Описание") }, maxLines = 4,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(4.dp), colors = fieldColors
                )
                Spacer(Modifier.height(24.dp))
            }
            Button(
                onClick = { keyboard?.hide(); library.createPlaylist(name, description, coverUri); onCreated() },
                enabled = name.isNotBlank(),
                modifier = Modifier.fillMaxWidth().padding(start = 17.dp, end = 17.dp, top = 16.dp, bottom = 32.dp).height(44.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PlaylistBlue, disabledContainerColor = Color(0xFFAEAFB4), disabledContentColor = Color.White)
            ) { Text("Создать") }
        }
    }
}
