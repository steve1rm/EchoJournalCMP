package org.example.echojournalcmp.core.presentation.util

fun Int.pad(length: Int = 2): String {
    return this.toString().padStart(length, '0')
}