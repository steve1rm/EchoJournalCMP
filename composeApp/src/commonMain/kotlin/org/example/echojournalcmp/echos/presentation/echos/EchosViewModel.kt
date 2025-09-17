package org.example.echojournalcmp.echos.presentation.echos
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import echojournalcmp.composeapp.generated.resources.Res
import echojournalcmp.composeapp.generated.resources.all_topics
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.echojournalcmp.core.presentation.designsystem.dropdowns.Selectable
import org.example.echojournalcmp.core.presentation.util.UiText
import org.example.echojournalcmp.echos.domain.recording.VoiceRecorder
import org.example.echojournalcmp.echos.presentation.echos.model.AudioCaptureMethod
import org.example.echojournalcmp.echos.presentation.model.MoodUi
import org.example.echojournalcmp.echos.presentation.echos.model.EchoFilterChip
import org.example.echojournalcmp.echos.presentation.echos.model.MoodChipContent
import org.example.echojournalcmp.echos.presentation.echos.model.RecordingState
import kotlin.time.Duration.Companion.seconds

class EchosViewModel(
    private val voiceRecorder: VoiceRecorder
) : ViewModel() {

    companion object {
        private val MIN_RECORDING_DURATION = 1.5.seconds
    }

    private var hasLoadedInitialData = false
    private val selectedMoodFilters = MutableStateFlow<List<MoodUi>>(emptyList())
    private val selectedTopicFilters = MutableStateFlow<List<String>>(emptyList())
    private val echoChannel = Channel<EchoEvents>()
    val echoEvents = echoChannel.receiveAsFlow()

    private val _state = MutableStateFlow(EchosState())
    val state = _state
        .onStart {
            if(!hasLoadedInitialData) {
                /** Load initial data here **/
                observeFilters()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = EchosState()
        )
        
        fun onAction(action: EchosAction) {
            when(action) {
                is EchosAction.OnRemoveFilters -> {
                    when(action.filterType) {
                        EchoFilterChip.MOODS -> {
                            selectedMoodFilters.update { emptyList() }
                        }
                        EchoFilterChip.TOPICS -> {
                            selectedTopicFilters.update { emptyList() }
                        }
                    }
                }
                EchosAction.OnTopicChipClick -> {
                    _state.update {
                        it.copy(
                            selectedEchoFilterChip = EchoFilterChip.TOPICS
                        )
                    }
                }

                EchosAction.OnMoodChipClick -> {
                    _state.update {
                        it.copy(
                            selectedEchoFilterChip = EchoFilterChip.MOODS
                        )
                    }
                }

                EchosAction.OnSettingsClick -> {

                }

                EchosAction.OnDismissMoodDropDown,
                EchosAction.OnDismissTopicDropDown -> {
                    _state.update {
                        it.copy(
                            selectedEchoFilterChip = null
                        )
                    }
                }
                is EchosAction.OnFilterByMoodClick -> {
                    toggleMoodFilter(action.moodUi)
                }
                is EchosAction.OnFilterByTopicClick -> {
                    toggleTopicFilter(action.topic)
                }

                EchosAction.OnPausedRecordingClick -> {
                    pauseRecording()
                }
                is EchosAction.OnRecordFabClick -> {
                    viewModelScope.launch {
                        echoChannel.send(EchoEvents.RequestAudioPermission)
                    }
                }
                is EchosAction.OnTrackSizeAvailable -> {

                }

                EchosAction.OnAudioPermissionGranted -> {
                    Logger.d(
                        tag = EchosViewModel::class.toString(),
                        messageString = "Recording has been started"
                    )
                    startRecording(captureMethod = AudioCaptureMethod.STANDARD)
                }

                EchosAction.OnCancelRecording -> {
                    cancelRecording()
                }
                EchosAction.OnCompletedRecording -> {
                    stopRecording()
                }
                EchosAction.OnResumedRecordingClick -> {
                    resumeRecording()
                }

                EchosAction.OnRecordButtonLongClick -> TODO()
                EchosAction.OnRequestPermissionQuickRecording -> TODO()
                is EchosAction.OnPlayEchoClick -> TODO()
            }
        }

    private fun pauseRecording() {
        voiceRecorder.pause()
        _state.update { it.copy(
            recordingState = RecordingState.PAUSED
        ) }
    }

    private fun resumeRecording() {
        voiceRecorder.resume()
        _state.update { it.copy(
            recordingState = RecordingState.NORMAL_CAPTURE
        ) }
    }

    private fun cancelRecording() {
        voiceRecorder.cancel()
        _state.update { it.copy(
            recordingState = RecordingState.NOT_RECORDING
        ) }
    }

    private fun stopRecording() {
        voiceRecorder.stop()
        val recordingDetails = voiceRecorder.recordingDetails.value

        if(recordingDetails.duration < MIN_RECORDING_DURATION) {
            viewModelScope.launch {
                echoChannel.send(EchoEvents.RecordingToShort)
            }
        }
        else {
            viewModelScope.launch {
                echoChannel.send(EchoEvents.RecordingCompleted)
            }
            _state.update { it.copy(
                recordingState = RecordingState.NOT_RECORDING
            ) }
        }
    }

    private fun startRecording(captureMethod: AudioCaptureMethod) {
        _state.update { it.copy(
            recordingState = when(captureMethod) {
                AudioCaptureMethod.STANDARD -> RecordingState.NORMAL_CAPTURE
                AudioCaptureMethod.QUICK -> RecordingState.QUICK_CAPTURE
            }
        ) }
        voiceRecorder.start()

        if(captureMethod == AudioCaptureMethod.STANDARD) {
            voiceRecorder
                .recordingDetails
                .distinctUntilChangedBy(
                    keySelector = { recordingDetails ->
                        recordingDetails.duration
                    }
                )
                .map { recordingDetails ->
                    recordingDetails.duration
                }
                .onEach { duration ->
                    _state.update { it.copy(
                        recordingElapsedDuration = duration
                    ) }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun toggleMoodFilter(moodUi: MoodUi) {
        selectedMoodFilters.update { selectedMoods ->
            if(moodUi in selectedMoods) {
                selectedMoods - moodUi
            }
            else {
                selectedMoods + moodUi
            }
        }
    }

    private fun toggleTopicFilter(topic: String) {
        selectedTopicFilters.update { selectedTopic ->
            if(selectedTopic.contains(topic)) {
                selectedTopic - topic
            }
            else {
                selectedTopic + topic
            }
        }
    }

    private fun observeFilters() {
        combine(
            selectedTopicFilters,
            selectedMoodFilters
        ) { selectedTopics, selectedMoods ->
            _state.update { echosState ->
                echosState.copy(
                    topics = echosState.topics.map { selectableTopic ->
                        Selectable(
                            item = selectableTopic.item,
                            selected = selectedTopics.contains(selectableTopic.item)
                        )
                    },
                    moods = MoodUi.entries.map { mood ->
                        Selectable(
                            item = mood,
                            selected = selectedMoods.contains(mood)
                        )
                    },
                    hasActiveMoodFilters = selectedMoods.isNotEmpty(),
                    hasActiveTopicFilters = selectedTopics.isNotEmpty(),
                    topicChipTitle = selectedTopics.deriveTopicsToText(),
                    moodChipContent = selectedMoods.asMoodChipContent()
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun List<String>.deriveTopicsToText(): UiText {
        return when(this.size) {
            0 -> UiText.LocalizedString(Res.string.all_topics)
            1 -> UiText.Dynamic(this.first())
            2 -> UiText.Dynamic("${this.first()}, ${this.last()}")
            else -> {
                val extraElementCount = this.size - 2
                UiText.Dynamic("${this.first()}, ${this[1]} +$extraElementCount")
            }
        }
    }

    private fun List<MoodUi>.asMoodChipContent(): MoodChipContent {
        val moodChipContent = if(this.isEmpty()) {
            MoodChipContent()
        }
        else {
            val icons = this.map { it.iconSet.fill }
            val moodNames = this.map { it.title }

            when(this.size) {
                1 -> MoodChipContent(
                    iconRes = icons,
                    title = moodNames.first()
                )
                2 -> MoodChipContent(
                    iconRes = icons,
                    title = UiText.Combined(
                        format = "%s %s",
                        uiTexts = moodNames.toTypedArray(),
                    )
                )
                else -> {
                    val extraElementCount = size - 2

                    MoodChipContent(
                        iconRes = icons,
                        title = UiText.Combined(
                            format = "%s %s +$extraElementCount",
                            uiTexts = moodNames.take(2).toTypedArray()
                        )
                    )
                }
            }
        }

        return moodChipContent
    }
}