package org.example.echojournalcmp

import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import org.example.echojournalcmp.core.database.EchoDatabase
import org.example.echojournalcmp.echos.domain.recording.VoiceRecorder
import org.koin.dsl.module
import platform.Foundation.NSHomeDirectory

val iosSpecificModule = module {
    factory<VoiceRecorder> {
        VoiceRecorderImp(
            applicationScope = get<CoroutineScope>()
        )
    }

    single<EchoDatabase> {
        val dbFile = NSHomeDirectory() + "/echo.db"

        Room.databaseBuilder<EchoDatabase>(
            name = dbFile,
        ).build()
    }
}