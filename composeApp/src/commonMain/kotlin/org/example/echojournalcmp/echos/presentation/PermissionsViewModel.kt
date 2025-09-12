package org.example.echojournalcmp.echos.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.microphone.RECORD_AUDIO
import kotlinx.coroutines.launch

class PermissionsViewModel(
    val permissionsController: PermissionsController
) : ViewModel() {

    var permissionState by mutableStateOf(PermissionState.NotDetermined)
        private set

    init {
        viewModelScope.launch {
             permissionState = permissionsController.getPermissionState(Permission.RECORD_AUDIO)
        }
    }

    fun provideOrRequestCameraPermission() {
        viewModelScope.launch {
            try {
                permissionsController.providePermission(Permission.RECORD_AUDIO)
                permissionState = PermissionState.Granted
            }
            catch (exception: DeniedAlwaysException) {
                permissionState = PermissionState.DeniedAlways
                exception.printStackTrace()
            }
            catch(exception: DeniedException) {
                permissionState = PermissionState.Denied
                exception.printStackTrace()
            }
            catch (exception: RequestCanceledException) {
                exception.printStackTrace()
            }
        }
    }
}