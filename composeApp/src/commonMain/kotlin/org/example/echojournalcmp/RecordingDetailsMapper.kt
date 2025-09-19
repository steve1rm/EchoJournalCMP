package org.example.echojournalcmp

import org.example.echojournalcmp.echos.domain.recording.RecordingDetails
import org.example.echojournalcmp.navigation.NavigationRoute
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant

fun RecordingDetails.toCreateEchoRoute(): NavigationRoute.CreateEchos {
    return NavigationRoute.CreateEchos(
        recordingPath = this.filePath ?: throw IllegalStateException("Recording path cannot be null"),
        duration = this.duration.inWholeMilliseconds,
        amplitudes = this.amplitudes.joinToString(";")
    )
}

fun NavigationRoute.CreateEchos.toRecordingDetails(): RecordingDetails {
    return RecordingDetails(
        filePath = this.recordingPath,
        duration = this.duration.milliseconds,
        amplitudes = this.amplitudes.split(";").map { it.toFloat() }
    )
}