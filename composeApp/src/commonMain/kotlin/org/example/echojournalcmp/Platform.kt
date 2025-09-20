package org.example.echojournalcmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.flow.StateFlow
import org.example.echojournalcmp.echos.domain.recording.RecordingDetails
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

@Composable
expect fun isAppInForeground() : State<Boolean>

expect val screenHeight: Int
@Composable
expect fun PlatformBackHandler(enabled: Boolean = true, onBack: () -> Unit)