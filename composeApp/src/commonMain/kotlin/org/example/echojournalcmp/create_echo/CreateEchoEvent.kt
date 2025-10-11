package org.example.echojournalcmp.create_echo

sealed interface CreateEchoEvent {
    data object FailedToSaveFile : CreateEchoEvent
    data object SuccessToSaveEcho : CreateEchoEvent
}