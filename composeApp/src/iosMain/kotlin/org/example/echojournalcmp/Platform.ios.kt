@file:OptIn(ExperimentalForeignApi::class)

package org.example.echojournalcmp

import androidx.compose.runtime.Composable
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.example.echojournalcmp.echos.domain.audio.AudioPlayer
import org.example.echojournalcmp.echos.domain.audio.AudioTrack
import org.example.echojournalcmp.echos.domain.echos.Mood
import org.example.echojournalcmp.echos.domain.recording.RecordingStorage
import org.example.echojournalcmp.echos.domain.settings.SettingsPreference
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

actual class SettingsPreferenceImp :
    SettingsPreference {
    actual override suspend fun saveDefaultTopics(topics: List<String>) {
    }

    actual override fun observeDefaultTopics(): Flow<List<String>> {
        TODO("Not yet implemented")
    }

    actual override suspend fun saveDefaultMood(mood: Mood) {
    }

    actual override fun observeDefaultMood(): Flow<Mood> {
        TODO("Not yet implemented")
    }
}