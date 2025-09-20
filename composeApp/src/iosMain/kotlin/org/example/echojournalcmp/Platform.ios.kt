@file:OptIn(ExperimentalForeignApi::class)

package org.example.echojournalcmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectGetHeight
import platform.UIKit.UIDevice
import platform.UIKit.UIScreen

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual val screenHeight: Int
    get() {
        val screen = UIScreen.mainScreen.bounds
        val scale = UIScreen.mainScreen.scale
        val heightPx = CGRectGetHeight(screen) * scale
        val density = scale.toFloat()

        return (heightPx / density).toInt()
    }
