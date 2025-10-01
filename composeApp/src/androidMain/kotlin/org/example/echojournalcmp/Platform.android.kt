@file:OptIn(ExperimentalTime::class)

package org.example.echojournalcmp

import android.content.Context
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import co.touchlab.kermit.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.example.echojournalcmp.echos.domain.recording.RecordingStorage
import java.io.File
import java.io.IOException
import kotlin.time.Clock
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