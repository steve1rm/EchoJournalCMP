@file:OptIn(FlowPreview::class)

package org.example.echojournalcmp.create_echo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.echojournalcmp.core.presentation.designsystem.dropdowns.Selectable.Companion.asUnselectedItems
import org.example.echojournalcmp.echos.domain.recording.RecordingStorage
import org.example.echojournalcmp.echos.presentation.echos.model.RecordingState
import org.example.echojournalcmp.echos.presentation.model.MoodUi
import org.example.echojournalcmp.navigation.NavigationRoute
import org.example.echojournalcmp.toRecordingDetails

class CreateEchoViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val recordingStorage: RecordingStorage
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val route = savedStateHandle.toRoute<NavigationRoute.CreateEchos>()
    private val recordingDetails = route.toRecordingDetails()

    private val _echoChannel = Channel<CreateEchoEvent>()
    val echoChannel = _echoChannel.receiveAsFlow()
    private val _state = MutableStateFlow(CreateEchoState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeAddTopicText()
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
            is CreateEchoAction.OnAddTopicTextChange -> onAddTopicTextChange(action.text)
            CreateEchoAction.OnConfirmMood -> onConfirmMood()
            CreateEchoAction.OnDismissMoodSelector -> onDismissMoodSelector()
            CreateEchoAction.OnDismissTopicSuggestions -> onDismissTopicSuggestion()
            is CreateEchoAction.OnMoodClick -> onMoodClick(action.moodUi)
            is CreateEchoAction.OnNoteTextChange -> TODO()
            CreateEchoAction.OnPauseAudioClick -> TODO()
            CreateEchoAction.OnPlayAudioClick -> TODO()
            is CreateEchoAction.OnRemoveTopicClick -> onRemoveTopicClick(action.topic)
            CreateEchoAction.OnSaveClick -> onSaveClick()
            CreateEchoAction.OnSelectMoodClick -> onSelectMoodClick()
            is CreateEchoAction.OnTitleTextChange -> onTitleTextChange(action.text)
            is CreateEchoAction.OnTopicClick -> onAddTopic(action.topic)
            is CreateEchoAction.OnTrackSizeAvailable -> TODO()
            CreateEchoAction.OnDismissConfirmLeaveDialog -> onDismissConfirmLeaveDialog()
            CreateEchoAction.OnCancelClick,
            CreateEchoAction.OnNavigateBackClick,
            CreateEchoAction.OnGoBack -> onShowLeaveDialog()
        }
    }

    private fun onTitleTextChange(text: String) {
        _state.update {
            it.copy(
                titleText = text
            )
        }
    }

    private fun onSaveClick() {
        if(recordingDetails.filePath != null) {
            viewModelScope.launch {
                val savedFilePath = recordingStorage.savePersistently(
                    tempFilePath = recordingDetails.filePath
                )

                if(savedFilePath == null) {
                    _echoChannel.send(CreateEchoEvent.FailedToSaveFile)
                }

                // TODO: Echo
            }
        }
    }

    private fun onShowLeaveDialog() {
        _state.update { it.copy(
            showConfirmLeaveDialog = true
        ) }
    }

    private fun onDismissConfirmLeaveDialog() {
        _state.update { it.copy(
            showConfirmLeaveDialog = false
        ) }
    }

    private fun observeAddTopicText() {
        state
            .map { state.value.addTopicText }
            .distinctUntilChanged()
            .debounce(300)
            .onEach { query ->
                _state.update { it.copy(
                    showTopicSuggestions = query.isNotBlank() && query.trim() !in state.value.topics,
                    searchResults = listOf("hello", "hello World").asUnselectedItems()

                ) }
            }
            .launchIn(viewModelScope)

    }

    private fun onDismissTopicSuggestion() {
        _state.update { it.copy(
            showTopicSuggestions = false
        ) }
    }

    private fun onRemoveTopicClick(topic: String) {
        _state.update { it.copy(
            topics = state.value.topics - topic
        ) }
    }

    private fun onAddTopic(topic: String) {
        _state.update { it.copy(
            addTopicText = "",
            topics = (state.value.topics + topic).distinct()
        ) }
    }

    private fun onAddTopicTextChange(text: String) {
        _state.update { it.copy(
            addTopicText = text.filter { text -> text.isLetterOrDigit() }
        ) }
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