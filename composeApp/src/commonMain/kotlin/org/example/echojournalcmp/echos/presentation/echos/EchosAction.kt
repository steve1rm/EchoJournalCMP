package org.example.echojournalcmp.echos.presentation.echos

import org.example.echojournalcmp.echos.presentation.echos.model.EchoFilterChip

sealed interface EchosAction {
    data object OnModelChipClick : EchosAction
    data object OnTopicChipClick : EchosAction
    data object OnFabClick : EchosAction
    data object OnFabLongClick : EchosAction
    data object OnSettingsClick : EchosAction
    data class OnRemoveFilters(val filterType: EchoFilterChip) : EchosAction
}
