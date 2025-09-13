package org.example.echojournalcmp.di


import org.example.echojournalcmp.echos.domain.recording.VoiceRecorder
import org.example.echojournalcmp.echos.presentation.echos.EchosViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val echoJournalModule = module {
    viewModel {
        EchosViewModel(
            get<VoiceRecorder>()
        )
    }
}