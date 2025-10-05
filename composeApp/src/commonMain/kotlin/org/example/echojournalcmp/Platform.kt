package org.example.echojournalcmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import org.example.echojournalcmp.echos.domain.audio.AudioPlayer
import org.example.echojournalcmp.echos.domain.audio.AudioTrack
import org.example.echojournalcmp.echos.domain.recording.RecordingDetails
import org.example.echojournalcmp.echos.domain.recording.RecordingStorage
import org.example.echojournalcmp.echos.domain.recording.VoiceRecorder

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect class VoiceRecorderImp : VoiceRecorder {
    override val recordingDetails: StateFlow<RecordingDetails>
    override fun start()
    override fun pause()
    override fun stop()
    override fun resume()
    override fun cancel()
}

expect class InternalRecordingStorageImp : RecordingStorage {
    override suspend fun savePersistently(tempFilePath: String): String?
    override suspend fun cleanUpTemporaryFiles()
}

expect class AudioPlayerImp : AudioPlayer {
    override val activeTrack: StateFlow<AudioTrack?>
    override fun play(filePath: String, onComplete: () -> Unit)
    override fun pause()
    override fun resume()
    override fun stop()
}

@Composable
expect fun isAppInForeground() : State<Boolean>

expect val screenHeight: Int
@Composable
expect fun PlatformBackHandler(enabled: Boolean = true, onBack: () -> Unit)