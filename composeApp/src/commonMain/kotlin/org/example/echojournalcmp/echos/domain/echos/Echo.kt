@file:OptIn(ExperimentalTime::class)

package org.example.echojournalcmp.echos.domain.echos

import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class Echo(
    val id: Int? = null,
    val mood: Mood,
    val title: String,
    val note: String,
    val topics: List<String>,
    val recordedAt: Instant,
    val audioFilePath: String,
    val audioPlaybackLength: Duration,
    val audioAmplitudes: List<Float>
)
