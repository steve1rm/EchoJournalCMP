@file:OptIn(FlowPreview::class, ExperimentalTime::class)

package org.example.echojournalcmp.create_echo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.echojournalcmp.core.presentation.designsystem.dropdowns.Selectable.Companion.asUnselectedItems
import org.example.echojournalcmp.echos.domain.audio.AudioPlayer
import org.example.echojournalcmp.echos.domain.echos.Echo
import org.example.echojournalcmp.echos.domain.echos.EchoDataSource
import org.example.echojournalcmp.echos.domain.echos.Mood
import org.example.echojournalcmp.echos.domain.recording.RecordingStorage
import org.example.echojournalcmp.echos.presentation.echos.model.PlaybackState
import org.example.echojournalcmp.echos.presentation.echos.model.TrackSizeInfo
import org.example.echojournalcmp.echos.presentation.model.MoodUi
import org.example.echojournalcmp.echos.presentation.utils.AmplitudeNormalizer
import org.example.echojournalcmp.navigation.NavigationRoute
import org.example.echojournalcmp.toRecordingDetails
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class CreateEchoViewModel(
    savedStateHandle: SavedStateHandle,
    private val recordingStorage: RecordingStorage,
    private val audioPlayer: AudioPlayer,
    private val echoDataSource: EchoDataSource
) : ViewModel() {

    private var hasLoadedInitialData = false

    private var durationJob: Job? = null

    private val route = savedStateHandle.toRoute<NavigationRoute.CreateEchos>()
    private val recordingDetails = route.toRecordingDetails()

    private val _echoChannel = Channel<CreateEchoEvent>()
    val echoChannel = _echoChannel.receiveAsFlow()
    private val _state = MutableStateFlow(CreateEchoState(
        playbackTotalDuration = recordingDetails.duration,
        titleText = savedStateHandle["title"] ?: "",
        noteText = savedStateHandle["note"] ?: "",
        topics = savedStateHandle.get<String>("topics")?.split(",") ?: emptyList(),
        mood = savedStateHandle.get<String>("mood")?.let { mood -> MoodUi.valueOf(mood) },
        showMoodSelector = savedStateHandle.get<String>("mood") == null,
        canSaveEcho = savedStateHandle["canSaveEcho"] ?: false,
    ))
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeAddTopicText()
                hasLoadedInitialData = true
            }
        }
        .onEach { state ->
            savedStateHandle["title"] = state.titleText
            savedStateHandle["note"] = state.noteText
            savedStateHandle["topics"] = state.topics.joinToString(",")
            savedStateHandle["mood"] = state.mood?.name
            savedStateHandle["canSaveEcho"] = state.canSaveEcho
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
            is CreateEchoAction.OnNoteTextChange -> onNoteTextChange(action.note)
            CreateEchoAction.OnPauseAudioClick -> onPauseAudio()
            CreateEchoAction.OnPlayAudioClick -> onPlayAudio()
            is CreateEchoAction.OnRemoveTopicClick -> onRemoveTopicClick(action.topic)
            CreateEchoAction.OnSaveClick -> onSaveClick()
            CreateEchoAction.OnSelectMoodClick -> onSelectMoodClick()
            is CreateEchoAction.OnTitleTextChange -> onTitleTextChange(action.text)
            is CreateEchoAction.OnTopicClick -> onAddTopic(action.topic)
            is CreateEchoAction.OnTrackSizeAvailable -> onTrackSizeAvailable(action.trackSizeInfo)
            CreateEchoAction.OnDismissConfirmLeaveDialog -> onDismissConfirmLeaveDialog()
            CreateEchoAction.OnCancelClick,
            CreateEchoAction.OnNavigateBackClick,
            CreateEchoAction.OnGoBack -> onShowLeaveDialog()
        }
    }

    private fun onNoteTextChange(text: String) {
        _state.update {
            it.copy(
                noteText = text
            )
        }
    }

    private fun onPauseAudio() {
        audioPlayer.pause()
    }

    private fun onPlayAudio() {
        if(state.value.playbackState == PlaybackState.PAUSED) {
            audioPlayer.resume()
        }
        else {
            audioPlayer.play(
                filePath = recordingDetails.filePath ?: throw IllegalStateException("File path cannot be null"),
                onComplete = {
                    _state.update { echoState ->
                        echoState.copy(
                            playbackState = PlaybackState.STOPPED,
                            durationPlayed = Duration.ZERO
                        )
                    }
                }
            )

            durationJob = audioPlayer.activeTrack
                .filterNotNull()
                .onEach {audioTrack ->
                    _state.update { echoState ->
                        echoState.copy(
                            playbackState = if(audioTrack.isPlaying) PlaybackState.PLAYING else PlaybackState.PAUSED,
                            durationPlayed = audioTrack.durationPlayed
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun onTrackSizeAvailable(trackSizeInfo: TrackSizeInfo) {
        viewModelScope.launch(Dispatchers.Default) {
            val finalAmplitudes = AmplitudeNormalizer.normalize(
                sourceAmplitudes = recordingDetails.amplitudes,
                trackWidth = trackSizeInfo.trackWidth,
                barWidth = trackSizeInfo.barWidth,
                spacing = trackSizeInfo.spacing
            )

            _state.update {
                it.copy(
                    playbackAmplitudes = finalAmplitudes
                )
            }
        }
    }

    private fun onTitleTextChange(text: String) {
        _state.update {
            it.copy(
                titleText = text,
                canSaveEcho = text.isNotBlank() && it.mood != null
            )
        }
    }

    private fun onSaveClick() {
        if(recordingDetails.filePath != null && state.value.canSaveEcho) {
            viewModelScope.launch {
                val savedFilePath = recordingStorage.savePersistently(
                    tempFilePath = recordingDetails.filePath
                )

                if(savedFilePath == null) {
                    _echoChannel.send(CreateEchoEvent.FailedToSaveFile)
                    return@launch
                }

                val currentState = state.value

                val echo = Echo(
                    mood = currentState.mood?.let {
                        Mood.valueOf(it.name)
                    } ?: throw IllegalStateException("Mood must be set before saving"),
                    title = currentState.titleText.trim(),
                    note = currentState.noteText.ifBlank { null },
                    topics = currentState.topics,
                    audioFilePath = savedFilePath,
                    audioPlaybackLength = currentState.playbackTotalDuration,
                    audioAmplitudes = recordingDetails.amplitudes,
                    recordedAt = Clock.System.now()
                )

                echoDataSource.insertEcho(echo)
                _echoChannel.send(CreateEchoEvent.SuccessToSaveEcho)
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