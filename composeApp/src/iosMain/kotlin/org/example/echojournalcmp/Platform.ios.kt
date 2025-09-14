package org.example.echojournalcmp

import androidx.compose.runtime.State
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun isAppInForeground(): State<Boolean> {
    TODO()
}