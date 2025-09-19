package org.example.echojournalcmp.create_echo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.echojournalcmp.core.presentation.designsystem.theme.EchoJournalCMPTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateEchoScene() {
    val createEchoViewModel = koinViewModel<CreateEchoViewModel>()
    val createEchoState by createEchoViewModel.state.collectAsStateWithLifecycle()

    CreateEchoScreen(
        state = createEchoState,
        onAction = createEchoViewModel::onAction
    )
}

@Preview
@Composable
private fun CreateEchoScenePreview() {
    EchoJournalCMPTheme {
        CreateEchoScene()
    }
}