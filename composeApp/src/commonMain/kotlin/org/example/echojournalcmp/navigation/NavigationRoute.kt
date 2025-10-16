package org.example.echojournalcmp.navigation

import kotlinx.serialization.Serializable

sealed interface NavigationRoute {
    @Serializable
    data class Echos(
        val startRecording: Boolean
    ) : NavigationRoute

    @Serializable
    data class CreateEchos(
        val recordingPath: String,
        val duration: Long,
        val amplitudes: String
    ) : NavigationRoute

    @Serializable
    data object Settings : NavigationRoute
}