package org.example.echojournalcmp.echos.presentation.settings

import org.example.echojournalcmp.echos.presentation.model.MoodUi

sealed interface SettingsAction {
    data class OnSearchTextChange(val text: String) : SettingsAction
    data object OnCreateTopicClick : SettingsAction
    data class OnRemoveTopicClick(val topic: String) : SettingsAction
    data object OnBackClick : SettingsAction
    data object OnDismissTopicDropDown : SettingsAction
    data object OnAddButtonClick : SettingsAction
    data class OnMoodClick(val mood: MoodUi) : SettingsAction
}