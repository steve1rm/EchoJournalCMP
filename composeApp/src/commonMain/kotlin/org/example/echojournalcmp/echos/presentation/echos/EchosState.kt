package org.example.echojournalcmp.echos.presentation.echos

import echojournalcmp.composeapp.generated.resources.Res
import echojournalcmp.composeapp.generated.resources.all_topics
import org.example.echojournalcmp.core.presentation.designsystem.dropdowns.Selectable
import org.example.echojournalcmp.core.presentation.designsystem.dropdowns.Selectable.Companion.asUnselectedItems
import org.example.echojournalcmp.core.presentation.util.UiText
import org.example.echojournalcmp.echos.presentation.model.MoodUi
import org.example.echojournalcmp.echos.presentation.echos.model.EchoFilterChip
import org.example.echojournalcmp.echos.presentation.echos.model.MoodChipContent

data class EchosState(
    val hasEchosRecorded: Boolean = false,
    val hasActiveTopicFilters: Boolean = false,
    val hasActiveMoodFilters: Boolean = false,
    val isLoadingData: Boolean = false,
    val moods: List<Selectable<MoodUi>> = emptyList<Selectable<MoodUi>>(),
    val topics: List<Selectable<String>> = listOf("Love", "Happy", "Work", "Travel", "Family").asUnselectedItems(),
    val moodChipContent: MoodChipContent = MoodChipContent(),
    val selectedEchoFilterChip: EchoFilterChip? = null,
    val topicChipTitle: UiText = UiText.LocalizedString(Res.string.all_topics)
)