package org.example.echojournalcmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNotificationCenter
import platform.UIKit.UIApplicationDidBecomeActiveNotification
import platform.UIKit.UIApplicationDidEnterBackgroundNotification
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun isAppInForeground(): State<Boolean> {
    return produceState(initialValue = true) {
        val observer = object : NSObject() {
            fun onForeground() {
                value = true
            }

            fun onBackground() {
                value = false
            }
        }

        val notificationCenter = NSNotificationCenter.defaultCenter

        notificationCenter.addObserverForName(
            name = UIApplicationDidBecomeActiveNotification,
            `object` = null,
            queue = null
        ) { _ ->
            observer.onForeground()
        }

        notificationCenter.addObserverForName(
            name = UIApplicationDidEnterBackgroundNotification,
            `object` = null,
            queue = null
        ) { _ ->
            observer.onBackground()
        }

        awaitDispose {
            notificationCenter.removeObserver(observer)
        }
    }
}