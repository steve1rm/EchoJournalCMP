package org.example.echojournalcmp.create_echo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.example.echojournalcmp.echos.presentation.model.MoodUi

class CreateEchoViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(CreateEchoState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CreateEchoState()
        )

    fun onAction(action: CreateEchoAction) {
        when (action) {
            is CreateEchoAction.OnAddTopicTextChange -> TODO()
            CreateEchoAction.OnCancelClick -> TODO()
            CreateEchoAction.OnConfirmMood -> onConfirmMood()
            CreateEchoAction.OnCreateNewTopicClick -> TODO()
            CreateEchoAction.OnDismissMoodSelector -> onDismissMoodSelector()
            CreateEchoAction.OnDismissTopicSuggestions -> TODO()
            is CreateEchoAction.OnMoodClick -> onMoodClick(action.moodUi)
            CreateEchoAction.OnNavigateBackClick -> TODO()
            is CreateEchoAction.OnNoteTextChange -> TODO()
            CreateEchoAction.OnPauseAudioClick -> TODO()
            CreateEchoAction.OnPlayAudioClick -> TODO()
            is CreateEchoAction.OnRemoveTopicClick -> TODO()
            CreateEchoAction.OnSaveClick -> TODO()
            CreateEchoAction.OnSelectMoodClick -> onSelectMoodClick()
            is CreateEchoAction.OnTitleTextChange -> TODO()
            is CreateEchoAction.OnTopicClick -> TODO()
            is CreateEchoAction.OnTrackSizeAvailable -> TODO()
        }
    }

    private fun onConfirmMood() {
        _state.update { it.copy(
            mood = it.selectedMood,
            canSaveEcho = it.titleText.isNotBlank(),
            showMoodSelector = false
        ) }
    }

    private fun onDismissMoodSelector() {
        _state.update { it.copy(
            showMoodSelector = false
        ) }
    }

    private fun onSelectMoodClick() {
        _state.update { it.copy(
            showMoodSelector = true
        ) }
    }

    private fun onMoodClick(mood: MoodUi) {
        _state.update { it.copy(
            selectedMood = mood
        ) }
    }

}