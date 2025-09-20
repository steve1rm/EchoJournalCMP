package org.example.echojournalcmp.create_echo

import org.example.echojournalcmp.core.presentation.designsystem.dropdowns.Selectable
import org.example.echojournalcmp.echos.presentation.echos.model.PlaybackState
import org.example.echojournalcmp.echos.presentation.model.MoodUi
import kotlin.time.Duration

data class CreateEchoState(
    val titleText: String = "",
    val addTopicText: String = "",
    val topics: List<String> = listOf("topic1", "topic2"),
    val noteText: String = "",
    val showMoodSelector: Boolean = true,
    val selectedMood: MoodUi = MoodUi.NEUTRAL,
    val showTopicSuggestions: Boolean = false,
    val mood: MoodUi? = null,
    val searchResults: List<Selectable<String>> = emptyList(),
    val showCreateTopicOption: Boolean = true,
    val canSaveEcho: Boolean = false,
    val playbackAmplitudes: List<Float> = List(32) { 0.3f },
    val playbackTotalDuration: Duration = Duration.ZERO,
    val playbackState: PlaybackState = PlaybackState.STOPPED,
    val durationPlayed: Duration = Duration.ZERO,
    val showConfirmLeaveDialog: Boolean = false
)

val CreateEchoState.durationPlayedRatio: Float
    get() {
        return (this.durationPlayed / this.playbackTotalDuration).toFloat()
    }