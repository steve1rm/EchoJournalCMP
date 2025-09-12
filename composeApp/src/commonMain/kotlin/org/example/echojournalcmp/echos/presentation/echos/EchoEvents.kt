package org.example.echojournalcmp.echos.presentation.echos

sealed interface EchoEvents {
    data object RequestAudioPermission : EchoEvents
}