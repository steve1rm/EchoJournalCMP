@file:OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)

package org.example.echojournalcmp

import android.content.Context
import android.media.MediaRecorder
import android.media.MediaRecorder.AudioSource
import android.os.Build
import android.telecom.VideoProfile.isPaused
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Contextual
import org.example.echojournalcmp.echos.domain.recording.RecordingDetails
import org.example.echojournalcmp.echos.domain.recording.RecordingStorage.Companion.RECORDING_FILE_EXTENSION
import org.example.echojournalcmp.echos.domain.recording.RecordingStorage.Companion.TEMP_FILE_PREFIX
import org.example.echojournalcmp.echos.domain.recording.VoiceRecorder
import java.io.File
import java.io.IOException
import kotlin.time.Duration.Companion.milliseconds
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

actual class VoiceRecorderImp(
    private val context: Context,
    private val applicationScope: CoroutineScope
) : VoiceRecorder {

    companion object {
        const val MAX_AMPLITUDE_VALUE = 20_000L
    }

    private val singleThreadDispatcher = Dispatchers.Default.limitedParallelism(1)

    private val _recordingDetails = MutableStateFlow(RecordingDetails())
    actual override val recordingDetails = _recordingDetails.asStateFlow()

    private var recorder: MediaRecorder? = null
    private var isRecording: Boolean = false
    private var isPaused: Boolean = false
    private val amplitudes = mutableListOf<Float>()
    private var tempFile = ""
    private var durationJob: Job? = null
    private var amplitudeJob: Job? = null

    actual override fun start() {
        if(isRecording) {
            return
        }

        tempFile = generateTempFile().absolutePath

        try {
            resetSession()

            recorder = newMediaRecorder().apply {
                this.setAudioSource(AudioSource.MIC)
                this.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                this.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                this.setAudioEncodingBitRate(128 * 1000)
                this.setAudioSamplingRate(44100)
                this.setOutputFile(tempFile)

                this.prepare()
                this.start()
            }
            isRecording = true
            isPaused = false

            startTrackingDuration()
            startTrackingAmplitudes()
        }
        catch (e: IOException) {
            Logger.e(
                tag = VoiceRecorderImp::class.simpleName ?: "",
                messageString = "${e.message}"
            )
            recorder?.release()
            recorder = null
        }
    }

    actual override fun pause() {
        if(!isRecording || isPaused) {
            return
        }
        recorder?.pause()
        isPaused = true
        durationJob?.cancel()
        amplitudeJob?.cancel()
    }

    actual override fun stop() {
        try {
            recorder?.stop()
            recorder?.release()
        }
        catch (exception: Exception) {
            Logger.e(
                tag = VoiceRecorderImp::class.toString(),
                messageString = "Failed to top recording $exception"
            )
        }
        finally {
            _recordingDetails.update {
                it.copy(
                    amplitudes = amplitudes.toList(),
                    filePath = tempFile
                )
            }
            cleanup()
        }
    }

    actual override fun resume() {
        if(!isRecording || !isPaused) {
            return
        }

        recorder?.resume()
        isPaused = false
        startTrackingDuration()
        startTrackingAmplitudes()
    }

    actual override fun cancel() {
        stop()
        resetSession()
    }

    private fun startTrackingAmplitudes() {
        amplitudeJob = applicationScope.launch {
            while(isRecording) {
                val amplitude = getAmplitude()
                withContext(singleThreadDispatcher) {
                    amplitudes.add(amplitude)
                }
            }
        }
    }

    private fun getAmplitude(): Float {
        return if(isRecording) {
            try {
                val maxAmplitude = recorder?.maxAmplitude
                val amplitudeRatio = maxAmplitude?.takeIf { it > 0F}?.run {
                    (this / MAX_AMPLITUDE_VALUE.toFloat().coerceIn(0F, 1F))
                }
                amplitudeRatio ?: 0F
            }
            catch(exception: Exception) {
                Logger.e(
                    tag = VoiceRecorderImp::class.toString(),
                    messageString = exception.localizedMessage ?: ""
                )
                0F
            }
        }
        else {
            0F
        }
    }

    private fun startTrackingDuration() {
        durationJob = applicationScope.launch {
            var lastTime = System.currentTimeMillis()

            while(isRecording && !isPaused) {
                delay(10L)
                val currentTime = System.currentTimeMillis()
                val elapsedTime = currentTime - lastTime

                _recordingDetails.update {
                    it.copy(
                        duration = it.duration + elapsedTime.milliseconds
                    )
                }

                lastTime = System.currentTimeMillis()
            }
        }
    }

    private fun generateTempFile(): File {
        val id = Uuid.random().toString()

        return File(
            context.cacheDir,
            "${TEMP_FILE_PREFIX}_$id.$RECORDING_FILE_EXTENSION"
        )

    }

    private fun newMediaRecorder(): MediaRecorder {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        }
        else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }
    }

    private fun resetSession() {
        _recordingDetails.update { RecordingDetails() }
        applicationScope.launch(singleThreadDispatcher) {
            amplitudes.clear()
            cleanup()
        }
    }

    private fun cleanup() {
        Logger.e(
            tag = VoiceRecorderImp::class.simpleName ?: "",
            messageString = "Cleaning up voice recorder"
        )
        recorder = null
        isRecording = false
        isPaused = false
        durationJob?.cancel()
        amplitudeJob?.cancel()
    }
}