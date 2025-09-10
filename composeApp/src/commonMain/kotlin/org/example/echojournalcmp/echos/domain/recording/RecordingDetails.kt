package org.example.echojournalcmp.echos.domain.recording

import kotlin.time.Duration

data class RecordingDetails(
    val duration: Duration = Duration.Companion.ZERO,
    val amplitudes: List<Float> = emptyList(),
    val filePath: String? = null
)