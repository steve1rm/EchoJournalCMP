package org.example.echojournalcmp

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController {
    initializeKoin(platformSpecificModules = arrayOf(iosSpecificModule))

    App()
}