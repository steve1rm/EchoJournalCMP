@file:OptIn(ExperimentalTime::class)

package org.example.echojournalcmp.echos.presentation.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


fun Instant.toReadableTime(): String {
    return this.toLocalDateTime(TimeZone.currentSystemDefault()).format(
        LocalDateTime.Format {
            this.hour(Padding.NONE)
            this.chars(":")
            this.minute(Padding.NONE)
        }
    )
}