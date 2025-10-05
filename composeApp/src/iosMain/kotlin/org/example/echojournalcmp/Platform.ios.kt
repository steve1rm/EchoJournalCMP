@file:OptIn(ExperimentalForeignApi::class)

package org.example.echojournalcmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.StateFlow
import org.example.echojournalcmp.echos.domain.audio.AudioPlayer
import org.example.echojournalcmp.echos.domain.audio.AudioTrack
import org.example.echojournalcmp.echos.domain.recording.RecordingStorage
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

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
}

actual class InternalRecordingStorageImp :
    RecordingStorage {
    actual override suspend fun savePersistently(tempFilePath: String): String? {
        TODO("Not yet implemented")
    }

    actual override suspend fun cleanUpTemporaryFiles() {
    }
}

actual class AudioPlayerImp :
    AudioPlayer {
    actual override val activeTrack: StateFlow<AudioTrack>
        get() = TODO("Not yet implemented")

    actual override fun play(filePath: String, onComplete: () -> Unit) {
    }

    actual override fun pause() {
    }

    actual override fun resume() {
    }

    actual override fun stop() {
    }
}