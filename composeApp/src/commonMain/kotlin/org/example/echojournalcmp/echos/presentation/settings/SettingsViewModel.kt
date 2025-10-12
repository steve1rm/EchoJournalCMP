package org.example.echojournalcmp.echos.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.echojournalcmp.echos.domain.echos.Mood
import org.example.echojournalcmp.echos.domain.settings.SettingsPreference
import org.example.echojournalcmp.echos.presentation.model.MoodUi

class SettingsViewModel(
    private val settingsPreference: SettingsPreference
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(SettingsState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeSettings()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SettingsState()
        )

    private fun observeSettings() {
        combine(
            settingsPreference.observeDefaultTopics(),
            settingsPreference.observeDefaultMood()
        ) { topics, mood ->
            _state.update { state ->
                state.copy(
                    topics = topics,
                    selectedMood = MoodUi.valueOf(mood.name)
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            SettingsAction.OnAddButtonClick -> {}
            is SettingsAction.OnSelectTopicClick -> onSelectTopicClick(action.topic)
            SettingsAction.OnDismissTopicDropDown -> {}
            is SettingsAction.OnMoodClick -> onMoodClick(action.mood)
            is SettingsAction.OnRemoveTopicClick -> onRemoveTopicClick(action.topic)
            is SettingsAction.OnSearchTextChange -> {}
            else -> Unit
        }
    }

    private fun onRemoveTopicClick(topic: String) {
        viewModelScope.launch {
            val newDefaultTopic = (state.value.topics - topic)
            settingsPreference.saveDefaultTopics(newDefaultTopic)
        }
    }

    private fun onSelectTopicClick(topic: String) {
        viewModelScope.launch {
            val newDefaultTopic = (state.value.topics + topic).distinct()
            settingsPreference.saveDefaultTopics(newDefaultTopic)
        }
    }

    private fun onMoodClick(mood: MoodUi) {
        viewModelScope.launch {
            settingsPreference.saveDefaultMood(Mood.valueOf(mood.name))
        }
    }
}