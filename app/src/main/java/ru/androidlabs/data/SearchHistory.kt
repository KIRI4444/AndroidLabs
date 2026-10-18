package ru.androidlabs.data

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import org.json.JSONArray

class SearchHistory(context: Context) {
    private val preferences = context.getSharedPreferences("search_history", Context.MODE_PRIVATE)
    val queries = mutableStateListOf<String>()

    init {
        val saved = preferences.getString("queries", null)
        if (saved == null) queries.addAll(listOf("Rammstein", "Queen", "Bruno Mars"))
        else runCatching {
            val array = JSONArray(saved)
            for (i in 0 until array.length()) queries.add(array.getString(i))
        }
    }

    fun add(query: String) {
        val value = query.trim()
        if (value.isEmpty()) return
        queries.removeAll { it.equals(value, ignoreCase = true) }
        queries.add(0, value)
        while (queries.size > 10) queries.removeAt(queries.lastIndex)
        preferences.edit().putString("queries", JSONArray(queries.toList()).toString()).apply()
    }
}
