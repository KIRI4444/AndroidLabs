package ru.androidlabs.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.alpha
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.androidlabs.R

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    fun open(intent: Intent) {
        try {
            context.startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(context, "На устройстве нет подходящего приложения", Toast.LENGTH_SHORT).show()
        }
    }
    ScreenLayout("Настройки", onBack) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            Spacer(Modifier.height(24.dp))
            Row(Modifier.fillMaxWidth().height(61.dp).padding(start = 16.dp, end = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Темная тема", Modifier.weight(1f), fontSize = 16.sp)
                Box(Modifier.size(56.dp, 40.dp), contentAlignment = Alignment.Center) {
                    Image(painterResource(R.drawable.ic_switch_track), null, Modifier.offset(x = (-1.5).dp).size(32.dp, 12.dp).alpha(0.6f))
                    Image(painterResource(R.drawable.ic_switch_knob), "Тёмная тема выключена", Modifier.offset(x = (-10).dp).size(18.dp).alpha(0.6f))
                }
            }
            SettingsRow("Поделиться приложением", R.drawable.ic_share) {
                open(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Playlist Maker — мои любимые треки и плейлисты")
                }, "Поделиться приложением"))
            }
            SettingsRow("Написать в поддержку", R.drawable.ic_support) {
                open(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:")).apply {
                    putExtra(Intent.EXTRA_SUBJECT, "Вопрос о Playlist Maker")
                    putExtra(Intent.EXTRA_TEXT, "Здравствуйте! У меня вопрос о приложении Playlist Maker:\n")
                })
            }
            SettingsRow("Пользовательское соглашение", R.drawable.ic_forward) {
                open(Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/")))
            }
        }
    }
}

@Composable
private fun SettingsRow(title: String, icon: Int, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = 61.dp).clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 18.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(title, Modifier.weight(1f), fontSize = 16.sp)
        Spacer(Modifier.width(8.dp))
        Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
            Icon(painterResource(icon), contentDescription = null, modifier = if (icon == R.drawable.ic_support) Modifier.size(20.dp, 18.dp) else Modifier.size(24.dp), tint = Color.Unspecified)
        }
    }
}
