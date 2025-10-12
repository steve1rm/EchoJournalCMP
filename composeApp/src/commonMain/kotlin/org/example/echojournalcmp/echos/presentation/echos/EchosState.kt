package org.example.echojournalcmp.echos.presentation.echos

import echojournalcmp.composeapp.generated.resources.Res
import echojournalcmp.composeapp.generated.resources.all_topics
import org.example.echojournalcmp.core.presentation.designsystem.dropdowns.Selectable
import org.example.echojournalcmp.core.presentation.designsystem.dropdowns.Selectable.Companion.asUnselectedItems
import org.example.echojournalcmp.core.presentation.util.UiText
import org.example.echojournalcmp.core.presentation.util.pad
import org.example.echojournalcmp.echos.presentation.echos.model.AudioCaptureMethod
import org.example.echojournalcmp.echos.presentation.echos.model.EchoDaySection
import org.example.echojournalcmp.echos.presentation.echos.model.EchoFilterChip
import org.example.echojournalcmp.echos.presentation.echos.model.MoodChipContent
import org.example.echojournalcmp.echos.presentation.echos.model.RecordingState
import org.example.echojournalcmp.echos.presentation.model.EchoUi
import org.example.echojournalcmp.echos.presentation.model.MoodUi
import kotlin.math.roundToInt
import kotlin.time.Duration

data class EchosState(
    val echos: Map<UiText, List<EchoUi>> = emptyMap(),
    val currentCapturedMethod: AudioCaptureMethod = AudioCaptureMethod.STANDARD,
    val recordingElapsedDuration: Duration = Duration.ZERO,
    val hasEchosRecorded: Boolean = false,
    val hasActiveTopicFilters: Boolean = false,
    val hasActiveMoodFilters: Boolean = false,
    val isLoadingData: Boolean = true,
    val moods: List<Selectable<MoodUi>> = emptyList<Selectable<MoodUi>>(),
    val topics: List<Selectable<String>> = listOf("Love", "Happy", "Work", "Travel", "Family").asUnselectedItems(),
    val moodChipContent: MoodChipContent = MoodChipContent(),
    val selectedEchoFilterChip: EchoFilterChip? = null,
    val topicChipTitle: UiText = UiText.LocalizedString(Res.string.all_topics),
    val recordingState: RecordingState = RecordingState.NOT_RECORDING
)

val EchosState.echoDaySections: List<EchoDaySection>
    get() {
        return this.echos
            .toList()
            .map { (dateHeader, echoes) ->
                EchoDaySection(dateHeader, echoes)
            }
    }

val EchosState.formattedRecordDuration: String
    get() {
        val minutes = (this.recordingElapsedDuration.inWholeMinutes.toInt())
        val seconds = (this.recordingElapsedDuration.inWholeSeconds % 60).toInt()
        val centiseconds = ((this.recordingElapsedDuration.inWholeMilliseconds % 1000) / 10.0).roundToInt()

        return "${minutes.pad()}:${seconds.pad()}:${centiseconds.pad()}"
    }

