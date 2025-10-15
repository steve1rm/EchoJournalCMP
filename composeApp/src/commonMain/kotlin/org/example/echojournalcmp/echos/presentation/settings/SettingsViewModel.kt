@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package org.example.echojournalcmp.echos.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.echojournalcmp.echos.domain.echos.EchoDataSource
import org.example.echojournalcmp.echos.domain.echos.Mood
import org.example.echojournalcmp.echos.domain.settings.SettingsPreference
import org.example.echojournalcmp.echos.presentation.model.MoodUi

class SettingsViewModel(
    private val settingsPreference: SettingsPreference,
    private val echoDataSource: EchoDataSource
) : ViewModel() {

    private var hasLoadedInitialData = false


    private val _state = MutableStateFlow(SettingsState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeSettings()
                observeTopicsSearchResults()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SettingsState()
        )

    private fun observeTopicsSearchResults() {
        state
            .distinctUntilChangedBy {
                it.searchText
            }
            .map {
                it.searchText
            }
            .debounce(300)
            .flatMapLatest { query ->
                if(query.isNotBlank()) {
                    echoDataSource.searchTopics(query)
                }
                else {
                    emptyFlow()
                }
            }
            .onEach { filteredResults ->
                _state.update {
                    val filteredNonDefaultResults = filteredResults - it.topics.toSet()
                    val searchText = it.searchText.trim()
                    val isNewTopic = searchText !in filteredNonDefaultResults && searchText !in it.topics
                            && searchText.isNotBlank()

                    it.copy(
                        suggestedTopics = filteredNonDefaultResults,
                        isTopicSuggestionsVisible = filteredResults.isNotEmpty() || isNewTopic,
                        showCreateTopicOption = isNewTopic
                    )
                }
            }
            .launchIn(viewModelScope)
    }

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
            SettingsAction.OnAddButtonClick -> onAddButtonClick()
            is SettingsAction.OnSelectTopicClick -> onSelectTopicClick(action.topic)
            SettingsAction.OnDismissTopicDropDown -> onDismissTopicDropDown()
            is SettingsAction.OnMoodClick -> onMoodClick(action.mood)
            is SettingsAction.OnRemoveTopicClick -> onRemoveTopicClick(action.topic)
            is SettingsAction.OnSearchTextChange -> onSearchTextChange(action.text)
            else -> Unit
        }
    }

    private fun onSearchTextChange(text: String) {
        _state.update {
            it.copy(
                searchText = text
            )
        }
    }

    private fun onDismissTopicDropDown() {
        _state.update {
            it.copy(
                isTopicSuggestionsVisible = false
            )
        }
    }

    private fun onAddButtonClick() {
        _state.update {
            it.copy(
                isTopicTextInputVisible = true
            )
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
            _state.update { state ->
                state.copy(
                    isTopicSuggestionsVisible = false,
                    isTopicTextInputVisible = false,
                    searchText = ""
                )
            }

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