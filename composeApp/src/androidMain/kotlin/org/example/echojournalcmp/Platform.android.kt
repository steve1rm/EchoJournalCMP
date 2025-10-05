@file:OptIn(ExperimentalTime::class)

package org.example.echojournalcmp

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.compose.dropUnlessResumed
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.example.echojournalcmp.echos.domain.audio.AudioPlayer
import org.example.echojournalcmp.echos.domain.audio.AudioTrack
import org.example.echojournalcmp.echos.domain.recording.RecordingStorage
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()


actual val screenHeight: Int
    @Composable
    get() {
        return LocalConfiguration.current.screenHeightDp
    }

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
    BackHandler(
        enabled = enabled,
        onBack = onBack
    )
}

actual class InternalRecordingStorageImp(
    private val context: Context) :
    RecordingStorage {
    actual override suspend fun savePersistently(tempFilePath: String): String? {
        val tempFile = File(tempFilePath)

        return if(!tempFile.exists()) {
            Logger.e {
                "The temporary file does not exist $tempFilePath"
            }
            null
        }
        else {
            withContext(Dispatchers.IO) {
                return@withContext try {
                    val savedFile = generateSavedFile()
                    tempFile.copyTo(savedFile)

                    savedFile.absolutePath
                }
                catch(exception: IOException) {
                    Logger.a {
                        exception.localizedMessage ?: "Unknown error"
                    }
                    null
                }
                finally {
                    withContext(NonCancellable) {
                        cleanUpTemporaryFiles()
                    }
                }
            }
        }
    }

    actual override suspend fun cleanUpTemporaryFiles() {
        withContext(Dispatchers.IO) {
            context.cacheDir
                .listFiles()
                ?.filter {
                    it.name.startsWith(RecordingStorage.TEMP_FILE_PREFIX)
                }
                ?.forEach { file ->
                    file.delete()
                }
        }
    }

    private fun generateSavedFile(): File {
        val timeStamp = Clock.System.now().epochSeconds

        return File(
            context.filesDir,
            "${RecordingStorage.PERSISTENT_FILE_PREFIX}_$timeStamp.${RecordingStorage.RECORDING_FILE_EXTENSION}"
        )
    }
}

actual class AudioPlayerImp(
    private val applicationScope: CoroutineScope) :
    AudioPlayer {

    private val _activeTrack = MutableStateFlow<AudioTrack?>(null)
    actual override val activeTrack: StateFlow<AudioTrack?> = _activeTrack.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null

    private var durationJob: Job? = null

    actual override fun play(filePath: String, onComplete: () -> Unit) {
        stop()

        mediaPlayer = MediaPlayer().apply {
            val fileInputStream = FileInputStream(File(filePath))

            try {
                setDataSource(filePath)

                prepare()
                start()

                _activeTrack.update {
                    AudioTrack(
                        isPlaying = true,
                        totalDuration = this.duration.milliseconds,
                        durationPlayed = Duration.ZERO
                    )
                }

                trackDuration()

                this.setOnCompletionListener {
                    onComplete()
                    stop()
                }
            }
            catch(exception: Exception) {
                Logger.e {
                    "Failed to playback audio ${exception.localizedMessage}"
                }
                fileInputStream.close()
            }
        }
    }

    actual override fun pause() {
        if(activeTrack.value?.isPlaying == true) {
            _activeTrack.update {
                it?.copy(
                    isPlaying = false)
            }
            durationJob?.cancel()
            mediaPlayer?.pause()
        }
    }

    actual override fun resume() {
        if(activeTrack.value?.isPlaying == false)
        _activeTrack.update {
            it?.copy(
                isPlaying = true)
        }
        mediaPlayer?.start()
        trackDuration()
    }

    actual override fun stop() {
        _activeTrack.update {
            it?.copy(
                isPlaying = false,
                durationPlayed = Duration.ZERO
            )
        }
        durationJob?.cancel()
        mediaPlayer?.apply {
            this.stop()
            this.reset()
            this.release()
        }
        mediaPlayer = null
    }

    private fun trackDuration() {
        durationJob?.cancel()

        durationJob = applicationScope.launch {
            do {
                _activeTrack.update {
                    it?.copy(
                        durationPlayed = mediaPlayer?.currentPosition?.milliseconds ?: Duration.ZERO
                    )
                }
                delay(10L)
            } while (activeTrack.value?.isPlaying == true && mediaPlayer?.isPlaying == true)
        }
    }
}