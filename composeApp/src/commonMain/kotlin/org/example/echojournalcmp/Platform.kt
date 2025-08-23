package org.example.echojournalcmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform