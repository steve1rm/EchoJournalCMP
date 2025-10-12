@file:OptIn(ExperimentalTime::class)

package org.example.echojournalcmp.echos.presentation.echos
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import echojournalcmp.composeapp.generated.resources.Res
import echojournalcmp.composeapp.generated.resources.all_topics
import echojournalcmp.composeapp.generated.resources.today
import echojournalcmp.composeapp.generated.resources.yesterday
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.echojournalcmp.core.presentation.designsystem.dropdowns.Selectable
import org.example.echojournalcmp.core.presentation.util.UiText
import org.example.echojournalcmp.echos.domain.audio.AudioPlayer
import org.example.echojournalcmp.echos.domain.echos.Echo
import org.example.echojournalcmp.echos.domain.echos.EchoDataSource
import org.example.echojournalcmp.echos.domain.recording.VoiceRecorder
import org.example.echojournalcmp.echos.presentation.echos.model.AudioCaptureMethod
import org.example.echojournalcmp.echos.presentation.echos.model.EchoFilterChip
import org.example.echojournalcmp.echos.presentation.echos.model.MoodChipContent
import org.example.echojournalcmp.echos.presentation.echos.model.PlaybackState
import org.example.echojournalcmp.echos.presentation.echos.model.RecordingState
import org.example.echojournalcmp.echos.presentation.echos.model.TrackSizeInfo
import org.example.echojournalcmp.echos.presentation.model.EchoUi
import org.example.echojournalcmp.echos.presentation.model.MoodUi
import org.example.echojournalcmp.echos.presentation.utils.AmplitudeNormalizer
import org.example.echojournalcmp.echos.presentation.utils.toEchoUI
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class EchosViewModel(
    private val voiceRecorder: VoiceRecorder,
    private val audioPlayer: AudioPlayer,
    private val echoDataSource: EchoDataSource
) : ViewModel() {

    companion object {
        private val MIN_RECORDING_DURATION = 1.5.seconds
    }

    private val playingEchoId = MutableStateFlow<Int?>(null)

    private var hasLoadedInitialData = false
    private val selectedMoodFilters = MutableStateFlow<List<MoodUi>>(emptyList())
    private val selectedTopicFilters = MutableStateFlow<List<String>>(emptyList())
    private val echoChannel = Channel<EchoEvents>()
    val echoEvents = echoChannel.receiveAsFlow()
    private val audioTrackSizeInfo = MutableStateFlow<TrackSizeInfo?>(null)

    private val _state = MutableStateFlow(EchosState())
    val state = _state
        .onStart {
            if(!hasLoadedInitialData) {
                /** Load initial data here **/
                observeFilters()
                observeEchos()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = EchosState()
        )

    private val echos = echoDataSource
        .observeEchos()
        .filterByMoodAndTopics()
        .onEach { echoes ->
            _state.update { state ->
                state.copy(
                    hasEchosRecorded = echoes.isNotEmpty(),
                    isLoadingData = false)
            }
        }
        .combine(audioTrackSizeInfo) { echos, trackSizeInfo ->
            echos.map { echo ->
                if(trackSizeInfo != null) {
                    echo.copy(
                        audioAmplitudes = AmplitudeNormalizer.normalize(
                            sourceAmplitudes = echo.audioAmplitudes,
                            trackWidth = trackSizeInfo.trackWidth,
                            barWidth = trackSizeInfo.barWidth,
                            spacing = trackSizeInfo.spacing
                        )
                    )
                }
                else {
                    echo
                }
            }
        }.flowOn(Dispatchers.Default)

    private fun observeEchos() {
        combine(
            echos,
            playingEchoId,
            audioPlayer.activeTrack
        ) { echos, echoId, activeTrack ->
            if(playingEchoId == null || activeTrack == null) {
                return@combine echos.map {
                    it.toEchoUI()
                }
            }

            echos.map { echo ->
                if(echo.id == playingEchoId.value) {
                    echo.toEchoUI(
                        currentPlaybackDuration = activeTrack.durationPlayed,
                        playbackState = if(activeTrack.isPlaying) PlaybackState.PLAYING else PlaybackState.PAUSED
                    )
                }
                else {
                    echo.toEchoUI()
                }
            }
        }
            .groupByRelativeDate()
            .onEach { groupedEchos ->
                _state.update { state ->
                    state.copy(
                        echos = groupedEchos
                    )
                }
            }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }
    private fun Flow<List<EchoUi>>.groupByRelativeDate(): Flow<Map<UiText, List<EchoUi>>> {
        val now = Clock.System.now()
        val todayDate = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val yesterdayDate = now.minus(1.days).toLocalDateTime(TimeZone.currentSystemDefault()).date

        return this.map { echoes ->
            echoes
                .groupBy { echoUi ->
                    Instant.fromEpochMilliseconds(
                        echoUi.recordedAt
                    ).toLocalDateTime(TimeZone.currentSystemDefault()).date
                }
                .mapValues { (_, echoList) ->
                    echoList.sortedByDescending { echoUi -> echoUi.recordedAt }
                }
                .toList()
                .sortedByDescending { (date, _) -> date }
                .associate { (date, echoList) ->
                    val dateText = when (date) {
                        todayDate -> UiText.LocalizedString(Res.string.today)
                        yesterdayDate -> UiText.LocalizedString(Res.string.yesterday)
                        else -> UiText.Dynamic(date.toString())
                    }
                    dateText to echoList
                }
        }
    }

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
                audioTrackSizeInfo.update {
                    action.trackSize
                }
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

            EchosAction.OnRecordButtonLongClick -> {
                startRecording(captureMethod = AudioCaptureMethod.QUICK)
            }
            EchosAction.OnRequestPermissionQuickRecording -> {
                viewModelScope.launch {
                    echoChannel.send(EchoEvents.RequestAudioPermission)
                }
            }
            is EchosAction.OnPlayEchoClick -> onPlayAudio(action.echoId)
        }
    }

    private fun onPlayAudio(echoId: Int) {
        val selectedEcho = state.value.echos.values
            .flatten()
            .first { echoUi -> echoUi.id == echoId }
        val activeTrack = audioPlayer.activeTrack.value
        val isNewEcho = echoId != playingEchoId.value
        val isPlayingTheSameEchoFromBeginning = echoId == playingEchoId.value && activeTrack != null && activeTrack.durationPlayed != Duration.ZERO

        when {
            isNewEcho || isPlayingTheSameEchoFromBeginning -> {
                audioPlayer.play(
                    filePath = selectedEcho.audioFilePath,
                    onComplete = ::onPlaybackCompleted
                )
            }
            else -> {
                audioPlayer.resume()
            }
        }
    }

    private fun onPlaybackCompleted() {
        _state.update { echosState ->
            echosState.copy(
                echos = echosState.echos.mapValues { (_, echoList) ->
                    echoList.map {
                        it.copy(
                            playbackTotalDuration = Duration.ZERO
                        )
                    }
                }
            )
        }

        playingEchoId.update { null }
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
                echoChannel.send(EchoEvents.RecordingCompleted(recordingDetails))
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
            echoDataSource.observeTopics(),
            selectedTopicFilters,
            selectedMoodFilters
        ) { allTopics, selectedTopics, selectedMoods ->
            _state.update { echosState ->
                echosState.copy(
                    topics = allTopics.map { selectableTopic ->
                        Selectable(
                            item = selectableTopic,
                            selected = selectedTopics.contains(selectableTopic)
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

    private fun Flow<List<Echo>>.filterByMoodAndTopics(): Flow<List<Echo>> {
        return combine(this, selectedMoodFilters, selectedTopicFilters) { echoList, selectedMoodFilters, selectedTopicFilters ->
            echoList.filter { echo ->
                val matchesMoodFilter = selectedMoodFilters
                    .takeIf { it.isNotEmpty() }
                    ?.any { it.name == echo.mood.name }
                    ?: true

                val matchesTopicFilters = selectedTopicFilters
                    .takeIf { it.isNotEmpty() }
                    ?.any {
                        it in echo.topics
                    }
                    ?: true

                matchesMoodFilter && matchesTopicFilters
            }
        }
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