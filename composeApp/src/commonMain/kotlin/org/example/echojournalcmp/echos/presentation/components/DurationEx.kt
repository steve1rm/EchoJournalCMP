package org.example.echojournalcmp.echos.presentation.components

import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

fun Duration.formatMMSS(): String {
    val totalSeconds = this.inWholeSeconds
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    return "${minutes}:${seconds}"
}
