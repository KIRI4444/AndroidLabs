package ru.androidlabs

import androidx.compose.ui.test.*
import androidx.compose.ui.graphics.asAndroidBitmap
import android.graphics.Bitmap
import java.io.File
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScreenFlowTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

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
    private fun screenshot(name: String) {
        val image = compose.onRoot().captureToImage().asAndroidBitmap()
        val folder = File(compose.activity.getExternalFilesDir(null), "screenshots").apply { mkdirs() }
        File(folder, "$name.png").outputStream().use { image.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }
}
