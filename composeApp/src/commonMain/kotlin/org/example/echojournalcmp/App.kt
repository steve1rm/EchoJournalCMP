package org.example.echojournalcmp

import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview

import org.example.echojournalcmp.core.presentation.designsystem.theme.EchoJournalCMPTheme
import org.example.echojournalcmp.echos.presentation.echos.EchosScene
import org.koin.core.context.startKoin

@Composable
@Preview
fun App() {
    EchoJournalCMPTheme {
        EchosScene()
    }
}