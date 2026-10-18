package ru.androidlabs.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.androidlabs.R
import ru.androidlabs.ui.theme.AndroidLabsTheme
import ru.androidlabs.ui.theme.PlaylistBlue
import ru.androidlabs.ui.theme.PlaylistBlack

@Composable
fun MainScreen(onOpenSection: (Int) -> Unit = {}) {
    Column(Modifier.fillMaxSize().background(PlaylistBlue).statusBarsPadding()) {
        Box(Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 16.dp), contentAlignment = Alignment.CenterStart) {
            Text(stringResource(R.string.app_name), color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(Modifier.height(14.dp))
        Column(
            Modifier.fillMaxSize()
                .background(Color.White, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .navigationBarsPadding().verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            MenuRow(R.string.search, R.drawable.ic_search) { onOpenSection(R.string.search) }
            MenuRow(R.string.playlists, R.drawable.ic_playlists) { onOpenSection(R.string.playlists) }
            MenuRow(R.string.favorites, R.drawable.ic_favorite) { onOpenSection(R.string.favorites) }
            MenuRow(R.string.settings, R.drawable.ic_settings) { onOpenSection(R.string.settings) }
        }
    }
}

@Composable
private fun MenuRow(@StringRes title: Int, @DrawableRes icon: Int, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().heightIn(min = 66.dp).clickable(onClick = onClick).padding(horizontal = 12.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painterResource(icon), contentDescription = null, modifier = Modifier.size(24.dp), tint = Color.Unspecified)
        Spacer(Modifier.width(8.dp))
        Text(stringResource(title), Modifier.weight(1f), color = PlaylistBlack, fontSize = 22.sp, fontWeight = FontWeight.Medium)
        Icon(painterResource(R.drawable.ic_forward), contentDescription = null, modifier = Modifier.size(24.dp), tint = Color.Unspecified)
    }
}

@Preview(widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun MainScreenPreview() {
    AndroidLabsTheme { MainScreen() }
}
