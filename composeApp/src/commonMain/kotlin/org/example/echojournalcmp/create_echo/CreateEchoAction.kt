package org.example.echojournalcmp.create_echo

import org.example.echojournalcmp.echos.presentation.echos.model.TrackSizeInfo
import org.example.echojournalcmp.echos.presentation.model.MoodUi

sealed interface CreateEchoAction {
    data object OnNavigateBackClick: CreateEchoAction
    data class OnTitleTextChange(val text: String): CreateEchoAction
    data class OnAddTopicTextChange(val text: String): CreateEchoAction
    data class OnNoteTextChange(val note: String): CreateEchoAction
    data object OnSelectMoodClick: CreateEchoAction
    data object OnDismissMoodSelector: CreateEchoAction
    data class OnMoodClick(val moodUi: MoodUi): CreateEchoAction
    data object OnConfirmMood: CreateEchoAction
    data class OnTopicClick(val topic: String): CreateEchoAction
    data object OnDismissTopicSuggestions: CreateEchoAction
    data object OnCancelClick: CreateEchoAction
    data object OnSaveClick: CreateEchoAction
    data object OnPlayAudioClick: CreateEchoAction
    data object OnPauseAudioClick: CreateEchoAction
    data class OnTrackSizeAvailable(val trackSizeInfo: TrackSizeInfo): CreateEchoAction
    data class OnRemoveTopicClick(val topic: String): CreateEchoAction
    data object OnGoBack : CreateEchoAction
    data object OnDismissConfirmLeaveDialog : CreateEchoAction
}