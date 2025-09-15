package org.example.echojournalcmp.echos.presentation.echos

sealed interface EchoEvents {
    data object RequestAudioPermission : EchoEvents
    data object RecordingToShort : EchoEvents
    data object RecordingCompleted : EchoEvents
}