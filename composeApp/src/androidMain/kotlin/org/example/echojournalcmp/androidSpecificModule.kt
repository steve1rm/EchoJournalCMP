package org.example.echojournalcmp

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import org.example.echojournalcmp.echos.domain.recording.VoiceRecorder
import org.koin.dsl.module

val androidSpecificModule = module {
    factory<VoiceRecorder> {
        VoiceRecorderImp(
            context = get<Context>(),
            applicationScope = get<CoroutineScope>()
        )
    }
}