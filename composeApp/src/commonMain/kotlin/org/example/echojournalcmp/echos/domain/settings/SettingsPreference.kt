package org.example.echojournalcmp.echos.domain.settings

import kotlinx.coroutines.flow.Flow
import org.example.echojournalcmp.echos.domain.echos.Mood

interface SettingsPreference {
    suspend fun saveDefaultTopics(topics: List<String>)
    fun observeDefaultTopics(): Flow<List<String>>

    suspend fun saveDefaultMood(mood: Mood)
    fun observeDefaultMood(): Flow<Mood>
}