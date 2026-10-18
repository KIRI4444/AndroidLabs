package ru.androidlabs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import ru.androidlabs.R
import ru.androidlabs.data.SearchHistory
import ru.androidlabs.data.Track
import ru.androidlabs.data.findTracks
import ru.androidlabs.ui.theme.PlaylistBlue
import ru.androidlabs.ui.theme.YsDisplay

@Composable
fun SearchScreen(onBack: () -> Unit, onTrack: (Track) -> Unit) {
    val context = LocalContext.current
    val history = remember { SearchHistory(context) }
    val keyboard = LocalSoftwareKeyboardController.current
    var query by rememberSaveable { mutableStateOf("") }
    var focused by remember { mutableStateOf(false) }
    var searchedQuery by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    LaunchedEffect(query) {
        loading = query.isNotBlank()
        if (loading) delay(350)
        searchedQuery = query.trim()
        loading = false
    }
    val error = searchedQuery.equals("ошибка", true) || searchedQuery.equals("error", true)
    val results = if (searchedQuery.isBlank() || error) emptyList() else findTracks(searchedQuery)
    ScreenLayout("Поиск", onBack) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp).background(Color(0xFFE6E8EB), RoundedCornerShape(8.dp))) {
            Row(Modifier.fillMaxWidth().height(36.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(painterResource(R.drawable.ic_search_small), null, Modifier.padding(start = 12.dp).size(16.dp), tint = Color.Unspecified)
                Spacer(Modifier.width(8.dp))
                BasicTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.weight(1f).onFocusChanged { focused = it.isFocused },
                    singleLine = true,
                    textStyle = TextStyle(fontFamily = YsDisplay, fontSize = 16.sp, color = Color(0xFF1A1B22)),
                    cursorBrush = SolidColor(PlaylistBlue),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { history.add(query); keyboard?.hide() }),
                    decorationBox = { inner ->
                        Box {
                            if (query.isEmpty()) Text("Поиск", color = Color(0xFFAEAFB4), fontSize = 16.sp)
                            inner()
                        }
                    }
                )
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }, modifier = Modifier.size(40.dp, 36.dp)) {
                        Icon(painterResource(R.drawable.ic_clear), "Очистить поиск", Modifier.size(16.dp), tint = Color.Unspecified)
                    }
                } else Spacer(Modifier.width(12.dp))
            }
            if (focused && query.isBlank() && history.queries.isNotEmpty()) {
                HorizontalDivider(Modifier.padding(horizontal = 9.dp), color = Color(0xFFCACBCD), thickness = 0.5.dp)
                history.queries.forEach { previous ->
                    Row(Modifier.fillMaxWidth().height(41.dp).clickable { query = previous; history.add(previous); keyboard?.hide() }.padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(painterResource(R.drawable.ic_history), null, Modifier.size(16.dp), tint = Color.Unspecified)
                        Spacer(Modifier.width(6.dp))
                        Text(previous, fontSize = 16.sp)
                    }
                }
            }
        }
        Box(Modifier.weight(1f).fillMaxWidth()) {
            when {
                query.isBlank() -> Unit
                loading -> CircularProgressIndicator(Modifier.align(Alignment.TopCenter).padding(top = 100.dp))
                error -> EmptyState("Проблемы со связью\n\nЗагрузка не удалась. Проверьте подключение к интернету", true, Modifier.padding(top = 102.dp))
                results.isEmpty() -> EmptyState("Ничего не нашлось", modifier = Modifier.padding(top = 102.dp))
                else -> LazyColumn(Modifier.fillMaxSize().imePadding(), contentPadding = PaddingValues(top = 16.dp)) {
                    items(results, key = { it.id }) { track ->
                        TrackRow(track) { history.add(query); keyboard?.hide(); onTrack(track) }
                    }
                }
            }
        }
    }
}
