package org.example.echojournalcmp

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.example.echojournalcmp.echos.domain.echos.Mood
import org.example.echojournalcmp.echos.domain.settings.SettingsPreference

actual class SettingsPreferenceImp(
    private val context: Context
) : SettingsPreference {

    companion object {
        private val Context.settingsDataStore by preferencesDataStore(
            name = "settings_datastore"
        )
    }

    private val topicsKey = stringSetPreferencesKey("topics")
    private val moodKey = stringPreferencesKey("mood")

    actual override suspend fun saveDefaultTopics(topics: List<String>) {
        context.settingsDataStore.edit { preferences ->
            preferences[topicsKey] = topics.toSet()
        }
    }

    actual override fun observeDefaultTopics(): Flow<List<String>> {
        return context.settingsDataStore.data
            .map { preferences ->
                preferences[topicsKey]?.toList() ?: emptyList()
            }
            .distinctUntilChanged()
    }

    actual override suspend fun saveDefaultMood(mood: Mood) {
        context.settingsDataStore.edit { preferences ->
            preferences[moodKey] = mood.name
        }
    }

    actual override fun observeDefaultMood(): Flow<Mood> {
        return context.settingsDataStore.data
            .map { preferences ->
                preferences[moodKey]?.let { mood ->
                    Mood.valueOf(mood)
                } ?: Mood.NEUTRAL
            }
            .distinctUntilChanged()
    }
}