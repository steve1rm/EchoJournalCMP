package org.example.echojournalcmp.echos.presentation.echos

import org.example.echojournalcmp.echos.model.MoodUi
import org.example.echojournalcmp.echos.presentation.echos.model.EchoFilterChip

sealed interface EchosAction {
    data object OnDismissMoodDropDown : EchosAction
    data object OnMoodChipClick : EchosAction
    data class OnFilterByMoodClick(val moodUi: MoodUi) : EchosAction
    data object OnTopicChipClick : EchosAction
    data class OnFilterByTopicClick(val topic: String) : EchosAction
    data object OnDismissTopicDropDown : EchosAction
    data object OnFabClick : EchosAction
    data object OnFabLongClick : EchosAction
    data object OnSettingsClick : EchosAction
    data class OnRemoveFilters(val filterType: EchoFilterChip) : EchosAction
}
