package org.example.echojournalcmp

import kotlinx.coroutines.flow.StateFlow
import org.example.echojournalcmp.echos.domain.recording.RecordingDetails
import org.example.echojournalcmp.echos.domain.recording.VoiceRecorder

actual class VoiceRecorderImp : VoiceRecorder {
    actual override val recordingDetails: StateFlow<RecordingDetails>
        get() = TODO("Not yet implemented")

    actual override fun start() {
        TODO("Not yet implemented")
    }

    actual override fun pause() {
        TODO("Not yet implemented")
    }

    actual override fun stop() {
        TODO("Not yet implemented")
    }

    actual override fun resume() {
        TODO("Not yet implemented")
    }

    actual override fun cancel() {
        TODO("Not yet implemented")
    }

}