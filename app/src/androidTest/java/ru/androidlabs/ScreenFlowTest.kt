package ru.androidlabs

import androidx.compose.ui.test.*
import android.graphics.Bitmap
import android.content.ContentValues
import android.provider.MediaStore
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.espresso.Espresso
import org.junit.rules.ExternalResource
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScreenFlowTest {
    @get:Rule(order = 0) val cleanData = object : ExternalResource() {
        override fun before() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            context.getSharedPreferences("music_library", 0).edit().clear().commit()
            context.getSharedPreferences("search_history", 0).edit().clear().commit()
        }
    }
    @get:Rule(order = 1) val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun searchShowsMatchesAndEmptyResult() {
        compose.onNodeWithText("Поиск").performClick()
        compose.onNode(hasSetTextAction()).performTextInput("The Beatles")
        compose.waitUntil(5000) { compose.onAllNodesWithText("Yesterday (Remastered 2009)").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("Yesterday (Remastered 2009)").assertIsDisplayed()
        screenshot("search")
        compose.onNodeWithContentDescription("Очистить поиск").performClick()
        compose.onNode(hasSetTextAction()).performTextInput("нет такого трека")
        compose.waitUntil(5000) { compose.onAllNodesWithText("Ничего не нашлось").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("Ничего не нашлось").assertIsDisplayed()
        screenshot("empty")
        compose.onNode(hasSetTextAction()).performTextReplacement("ошибка")
        compose.waitUntil(5000) { compose.onAllNodesWithText("Проблемы со связью", substring = true).fetchSemanticsNodes().isNotEmpty() }
        screenshot("error")
    }

    @Test
    fun settingsReturnsToMainMenu() {
        screenshot("main")
        compose.onNodeWithText("Настройки").performClick()
        compose.onNodeWithText("Поделиться приложением").assertIsDisplayed()
        screenshot("settings")
        compose.onNodeWithContentDescription("Назад").performClick()
        compose.onNodeWithText("Playlist Maker").assertIsDisplayed()
    }
    @Test
    fun playerAddsFavoriteAndKeepsSearchWhenReturning() {
        compose.onNodeWithText("Поиск").performClick()
        compose.onNode(hasSetTextAction()).performTextInput("Yesterday")
        compose.waitUntil(5000) { compose.onAllNodesWithText("Yesterday (Remastered 2009)").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("Yesterday (Remastered 2009)").performClick()
        compose.onNodeWithContentDescription("В избранное").performClick()
        compose.onNodeWithContentDescription("Убрать из избранного").assertExists()
        screenshot("player")
        compose.onNodeWithContentDescription("Назад").performClick()
        compose.onNode(hasSetTextAction()).assertTextEquals("Yesterday")
        compose.onNodeWithContentDescription("Назад").performClick()
        compose.onNodeWithText("Избранное").performClick()
        compose.onNodeWithText("Yesterday (Remastered 2009)").assertIsDisplayed()
        screenshot("favorites")
        Espresso.pressBack()
        compose.onNodeWithText("Playlist Maker").assertIsDisplayed()
    }

    @Test
    fun createsPlaylistAndAddsTrack() {
        compose.onNodeWithText("Плейлисты").performClick()
        screenshot("playlists")
        compose.onNodeWithContentDescription("Новый плейлист").performClick()
        compose.onNodeWithText("Создать").assertIsNotEnabled()
        screenshot("new_playlist_empty")
        compose.onNodeWithText("Название*").performTextInput("   ")
        compose.onNodeWithText("Создать").assertIsNotEnabled()
        compose.onNodeWithText("Название*").performTextReplacement("Для прогулки")
        compose.onNodeWithText("Описание").performTextInput("Любимые песни")
        compose.onNodeWithText("Создать").assertIsEnabled()
        screenshot("new_playlist_filled")
        compose.onNodeWithText("Создать").performClick()
        compose.onNodeWithText("Для прогулки").assertIsDisplayed()
        compose.onNodeWithContentDescription("Назад").performClick()
        compose.onNodeWithText("Поиск").performClick()
        compose.onNode(hasSetTextAction()).performTextInput("Yesterday")
        compose.waitUntil(5000) { compose.onAllNodesWithText("Yesterday (Remastered 2009)").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("Yesterday (Remastered 2009)").performClick()
        compose.onNodeWithContentDescription("Добавить в плейлист").performClick()
        compose.onNodeWithText("Для прогулки").assertIsDisplayed()
        screenshot("add_to_playlist")
        compose.onNodeWithText("Для прогулки").performClick()
        compose.waitUntil(5000) { compose.onAllNodesWithText("Добавить в плейлист").fetchSemanticsNodes().isEmpty() }
        compose.onNodeWithContentDescription("Назад").performClick()
        compose.onNodeWithContentDescription("Назад").performClick()
        compose.onNodeWithText("Плейлисты").performClick()
        compose.onNodeWithText("Для прогулки").performClick()
        compose.onNodeWithText("Yesterday (Remastered 2009)").assertIsDisplayed()
        screenshot("playlist_details")
        compose.activityRule.scenario.recreate()
        compose.onNodeWithText("Для прогулки").assertIsDisplayed()
        compose.onNodeWithText("Yesterday (Remastered 2009)").assertIsDisplayed()
    }

    @Test
    fun favoritesStartsEmpty() {
        compose.onNodeWithText("Избранное").performClick()
        compose.onNodeWithText("Ваша медиатека пуста").assertIsDisplayed()
        screenshot("favorites_empty")
    }

    private fun screenshot(name: String) {
        compose.waitForIdle()
        val image = InstrumentationRegistry.getInstrumentation().uiAutomation.takeScreenshot()
        val resolver = compose.activity.contentResolver
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$name.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/PlaylistMaker")
        }
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
        resolver.openOutputStream(uri)!!.use { image.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }
}
