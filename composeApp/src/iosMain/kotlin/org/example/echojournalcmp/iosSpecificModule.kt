package org.example.echojournalcmp

import kotlinx.coroutines.CoroutineScope
import org.example.echojournalcmp.echos.domain.recording.VoiceRecorder
import org.koin.dsl.module

val iosSpecificModule = module {
    factory<VoiceRecorder> {
        VoiceRecorderImp(
            applicationScope = get<CoroutineScope>()
        )
    }
}