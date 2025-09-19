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
import org.example.echojournalcmp.echos.domain.recording.RecordingDetails
import org.example.echojournalcmp.echos.presentation.PermissionsViewModel
import org.example.echojournalcmp.echos.presentation.echos.model.RecordingState
import org.example.echojournalcmp.isAppInForeground
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EchosScene(
    onNavigateToCreateEcho: (recordingDetails: RecordingDetails) -> Unit
) {
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
            EchoEvents.RecordingToShort -> {
                Logger.d {
                    "Recording too short"
                }
            }

            is EchoEvents.RecordingCompleted -> {
                onNavigateToCreateEcho(events.recordingDetails)
            }
        }
    }

    val isAppInBackground by isAppInForeground()
    LaunchedEffect(isAppInBackground, state.recordingState) {
        if(!isAppInBackground && state.recordingState == RecordingState.NORMAL_CAPTURE) {
            echosViewModel.onAction(EchosAction.OnPausedRecordingClick)
        }
    }

    EchosScreen(
        state = state,
        onAction = echosViewModel::onAction
    )
}