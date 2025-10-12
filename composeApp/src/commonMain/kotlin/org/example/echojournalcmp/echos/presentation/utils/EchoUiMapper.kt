@file:OptIn(ExperimentalTime::class)

package org.example.echojournalcmp.echos.presentation.utils

import org.example.echojournalcmp.echos.domain.echos.Echo
import org.example.echojournalcmp.echos.presentation.echos.model.PlaybackState
import org.example.echojournalcmp.echos.presentation.model.EchoUi
import org.example.echojournalcmp.echos.presentation.model.MoodUi
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

fun Echo.toEchoUI(
    currentPlaybackDuration: Duration = Duration.ZERO,
    playbackState: PlaybackState = PlaybackState.STOPPED
): EchoUi {
    return EchoUi(
        id = this.id!!,
        title = this.title,
        mood = MoodUi.valueOf(this.mood.name),
        recordedAt = this.recordedAt.toEpochMilliseconds(),
        note = this.note,
        topics = this.topics,
        amplitudes = this.audioAmplitudes,
        audioFilePath = this.audioFilePath,
        playbackTotalDuration = this.audioPlaybackLength,
        playbackCurrentDuration = currentPlaybackDuration,
        playbackState = playbackState
    )
}