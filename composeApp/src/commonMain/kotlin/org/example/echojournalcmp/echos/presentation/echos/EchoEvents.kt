package org.example.echojournalcmp.echos.presentation.echos

import org.example.echojournalcmp.echos.domain.recording.RecordingDetails

sealed interface EchoEvents {
    data object RequestAudioPermission : EchoEvents
    data object RecordingToShort : EchoEvents
    data class RecordingCompleted(val recordingDetails: RecordingDetails) : EchoEvents
}