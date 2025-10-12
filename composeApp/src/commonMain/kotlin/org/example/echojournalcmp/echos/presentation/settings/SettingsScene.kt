package org.example.echojournalcmp.echos.presentation.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScene(
    onBackClick: () -> Unit
) {
    val settingsViewModel = koinViewModel<SettingsViewModel>()
    val state by settingsViewModel.state.collectAsStateWithLifecycle()

    SettingsScreen(
        state = state,
        onAction = { action ->
            when(action) {
                is SettingsAction.OnBackClick -> {
                    onBackClick()
                }
                else -> {
                    settingsViewModel.onAction(action)
                }
            }
        }
    )
}