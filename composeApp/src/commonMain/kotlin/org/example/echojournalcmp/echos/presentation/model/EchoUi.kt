@file:OptIn(ExperimentalTime::class)

package org.example.echojournalcmp.echos.presentation.model

import org.example.echojournalcmp.echos.presentation.echos.model.PlaybackState
import org.example.echojournalcmp.echos.presentation.utils.toReadableTime
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class EchoUi(
    val id: Int,
    val title: String,
    val mood: MoodUi,
    val recordedAt: Long,
    val note: String?,
    val topics: List<String>,
    val amplitudes: List<Float>,
    val playbackTotalDuration: Duration,
    val playbackCurrentDuration: Duration = Duration.ZERO,
    val playbackState: PlaybackState = PlaybackState.STOPPED,
    val audioFilePath: String = ""
) {
    val formattedRecordedAt = Instant
        .fromEpochMilliseconds(recordedAt)
        .toReadableTime()

    val playbackRatio = (playbackCurrentDuration / playbackTotalDuration).toFloat()
}
