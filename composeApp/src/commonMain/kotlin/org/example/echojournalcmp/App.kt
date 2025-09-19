package org.example.echojournalcmp

import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview

import org.example.echojournalcmp.core.presentation.designsystem.theme.EchoJournalCMPTheme
import org.example.echojournalcmp.echos.presentation.echos.EchosScene
import org.example.echojournalcmp.navigation.NavigationHost
import org.koin.core.context.startKoin

@Composable
@Preview
fun App() {
    EchoJournalCMPTheme {
        val navHostController = rememberNavController()

        NavigationHost(
            navigationController = navHostController
        )
    }
}