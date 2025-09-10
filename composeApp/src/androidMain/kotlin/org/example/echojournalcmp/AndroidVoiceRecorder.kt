package org.example.echojournalcmp

import kotlinx.coroutines.flow.StateFlow
import org.example.echojournalcmp.echos.domain.recording.RecordingDetails
import org.example.echojournalcmp.echos.domain.recording.VoiceRecorder

class AndroidVoiceRecorder : VoiceRecorder {
    override val recordingDetails: StateFlow<RecordingDetails>
        get() = TODO("Not yet implemented")

    override fun start() {
        TODO("Not yet implemented")
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun cancel() {
        TODO("Not yet implemented")
    }
}