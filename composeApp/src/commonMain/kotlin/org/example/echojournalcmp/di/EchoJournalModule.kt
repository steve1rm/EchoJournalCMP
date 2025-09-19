package org.example.echojournalcmp.di


import org.example.echojournalcmp.create_echo.CreateEchoViewModel
import org.example.echojournalcmp.echos.domain.recording.VoiceRecorder
import org.example.echojournalcmp.echos.presentation.echos.EchosViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val echoJournalModule = module {
    viewModelOf(::EchosViewModel)
    viewModelOf(::CreateEchoViewModel)
}