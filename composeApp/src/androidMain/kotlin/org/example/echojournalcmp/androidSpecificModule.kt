package org.example.echojournalcmp

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import org.example.echojournalcmp.echos.domain.audio.AudioPlayer
import org.example.echojournalcmp.echos.domain.recording.RecordingStorage
import org.example.echojournalcmp.echos.domain.recording.VoiceRecorder
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val androidSpecificModule = module {
    factory<VoiceRecorder> {
        VoiceRecorderImp(
            context = get<Context>(),
            applicationScope = get<CoroutineScope>()
        )
    }

    singleOf(::InternalRecordingStorageImp) bind RecordingStorage::class
    singleOf(::AudioPlayerImp) bind AudioPlayer::class
}