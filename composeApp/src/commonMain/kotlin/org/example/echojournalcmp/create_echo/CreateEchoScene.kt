package org.example.echojournalcmp.create_echo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import org.example.echojournalcmp.core.presentation.designsystem.theme.EchoJournalCMPTheme
import org.example.echojournalcmp.core.presentation.util.ObserveAsEvents
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateEchoScene(
    onConfirmLeave: () -> Unit
) {
    val createEchoViewModel = koinViewModel<CreateEchoViewModel>()
    val createEchoState by createEchoViewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(createEchoViewModel.echoChannel) { createEchoEvent ->
        when(createEchoEvent) {
            CreateEchoEvent.FailedToSaveFile -> {
                Logger.e {
                    "Failed to save file"
                }
                // TODO show a toast here
                onConfirmLeave()
            }

            CreateEchoEvent.SuccessToSaveEcho -> {
                onConfirmLeave()
            }
        }
    }

    CreateEchoScreen(
        state = createEchoState,
        onAction = createEchoViewModel::onAction,
        onConfirmLeave = onConfirmLeave
    )
}

@Preview
@Composable
private fun CreateEchoScenePreview() {
    EchoJournalCMPTheme {
        CreateEchoScene {

        }
    }
}