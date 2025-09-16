package org.example.echojournalcmp.echos.presentation.echos

import org.example.echojournalcmp.echos.presentation.model.MoodUi
import org.example.echojournalcmp.echos.presentation.echos.model.EchoFilterChip
import org.example.echojournalcmp.echos.presentation.echos.model.TrackSizeInfo

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
    data object OnRecordFabClick : EchosAction
    data object OnRecordButtonLongClick : EchosAction
    data object OnPausedRecordingClick : EchosAction
    data object OnResumedRecordingClick : EchosAction
    data class OnTrackSizeAvailable(val trackSize: TrackSizeInfo) : EchosAction
    data object OnAudioPermissionGranted : EchosAction
    data object OnCancelRecording : EchosAction
    data object OnCompletedRecording : EchosAction
    data object OnRequestPermissionQuickRecording: EchosAction
}
