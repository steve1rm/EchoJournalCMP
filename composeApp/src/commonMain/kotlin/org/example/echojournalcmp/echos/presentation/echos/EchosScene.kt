package org.example.echojournalcmp.echos.presentation.echos

import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import co.touchlab.kermit.Logger
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import org.example.echojournalcmp.core.presentation.util.ObserveAsEvents
import org.example.echojournalcmp.echos.presentation.PermissionsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EchosScene() {
    val echosViewModel = koinViewModel<EchosViewModel>()
    val state by echosViewModel.state.collectAsStateWithLifecycle()

    val permissionsController = rememberPermissionsControllerFactory()
    val permissionsViewModel = viewModel(
        initializer = {
            PermissionsViewModel(permissionsController.createPermissionsController())
        })
    BindEffect(permissionsViewModel.permissionsController)

    ObserveAsEvents(echosViewModel.echoEvents) { events ->
        when(events) {
            EchoEvents.RequestAudioPermission -> {
                if(permissionsViewModel.permissionState == PermissionState.Granted) {
                    echosViewModel.onAction(EchosAction.OnAudioPermissionGranted)
                }
                else {
                    permissionsViewModel.provideOrRequestCameraPermission()
                }
            }

            EchoEvents.RecordingCompleted -> {
                Logger.d {
                    "Recording Completed"
                }

            }
            EchoEvents.RecordingToShort -> {
                Logger.d {
                    "Recording too short"
                }
            }
        }
    }

    EchosScreen(
        state = state,
        onAction = echosViewModel::onAction
    )
}