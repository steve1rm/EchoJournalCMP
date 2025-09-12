@file:OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class, ExperimentalForeignApi::class, BetaInteropApi::class)

package org.example.echojournalcmp

import co.touchlab.kermit.Logger
import kotlinx.cinterop.*
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
import org.example.echojournalcmp.echos.domain.recording.RecordingDetails
import org.example.echojournalcmp.echos.domain.recording.VoiceRecorder
import platform.AVFAudio.*
import platform.Foundation.*
import platform.CoreAudioTypes.kAudioFormatMPEG4AAC
import kotlin.time.Duration.Companion.milliseconds
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

actual class VoiceRecorderImp(
    private val applicationScope: CoroutineScope
) : VoiceRecorder {

    companion object {
        const val TEMP_FILE_PREFIX = "temp_file_prefix"
        const val MAX_AMPLITUDE_VALUE = 160.0 // Peak power range is typically 0 to -160dB
    }

    private val singleThreadDispatcher = Dispatchers.Default.limitedParallelism(1)

    private val _recordingDetails = MutableStateFlow(RecordingDetails())
    actual override val recordingDetails: StateFlow<RecordingDetails> = _recordingDetails.asStateFlow()

    private var audioRecorder: AVAudioRecorder? = null
    private var isRecording: Boolean = false
    private var isPaused: Boolean = false
    private val amplitudes = mutableListOf<Float>()
    private var tempFileUrl: NSURL? = null
    private var durationJob: Job? = null
    private var amplitudeJob: Job? = null

    actual override fun start() {
        if (isRecording) {
            return
        }

        try {
            resetSession()
            setupAudioSession()

            tempFileUrl = generateTempFileUrl()

            val settings = mapOf(
                AVFormatIDKey to NSNumber.numberWithUnsignedInt(kAudioFormatMPEG4AAC),
                AVEncoderAudioQualityKey to NSNumber.numberWithInt(AVAudioQualityHigh.toInt()),
                AVEncoderBitRateKey to NSNumber.numberWithInt(128000),
                AVSampleRateKey to NSNumber.numberWithDouble(44100.0),
                AVNumberOfChannelsKey to NSNumber.numberWithInt(1)
            )

            memScoped {
                val error = alloc<ObjCObjectVar<NSError?>>()
                audioRecorder = AVAudioRecorder(
                    uRL = tempFileUrl!!,
                    settings = settings as Map<Any?, *>,
                    error = error.ptr
                )

                error.value?.let { err ->
                    Logger.e(
                        tag = VoiceRecorderImp::class.simpleName ?: "",
                        messageString = "Failed to create AVAudioRecorder: ${err.localizedDescription}"
                    )
                    return
                }
            }

            audioRecorder?.let { recorder ->
                recorder.meteringEnabled = true
                recorder.prepareToRecord()

                if (recorder.record()) {
                    isRecording = true
                    isPaused = false
                    startTrackingDuration()
                    startTrackingAmplitudes()
                } else {
                    Logger.e(
                        tag = VoiceRecorderImp::class.simpleName ?: "",
                        messageString = "Failed to start recording"
                    )
                }
            }
        } catch (e: Exception) {
            Logger.e(
                tag = VoiceRecorderImp::class.simpleName ?: "",
                messageString = "Error starting recording: ${e.message}"
            )
            cleanup()
        }
    }

    actual override fun pause() {
        if (!isRecording || isPaused) {
            return
        }

        audioRecorder?.pause()
        isPaused = true
        durationJob?.cancel()
        amplitudeJob?.cancel()
    }

    actual override fun resume() {
        if (!isRecording || !isPaused) {
            return
        }

        if (audioRecorder?.record() == true) {
            isPaused = false
            startTrackingDuration()
            startTrackingAmplitudes()
        }
    }

    actual override fun stop() {
        try {
            audioRecorder?.stop()

            _recordingDetails.update {
                it.copy(
                    amplitudes = amplitudes.toList(),
                    filePath = tempFileUrl?.path
                )
            }
        } catch (exception: Exception) {
            Logger.e(
                tag = VoiceRecorderImp::class.simpleName ?: "",
                messageString = "Failed to stop recording: $exception"
            )
        } finally {
            cleanup()
        }
    }

    actual override fun cancel() {
        stop()
        resetSession()

        // Delete temp file
        tempFileUrl?.let { url ->
            memScoped {
                val error = alloc<ObjCObjectVar<NSError?>>()
                NSFileManager.defaultManager.removeItemAtURL(url, error.ptr)
            }
        }
    }

    private fun startTrackingAmplitudes() {
        amplitudeJob = applicationScope.launch {
            while (isRecording && !isPaused) {
                delay(100L) // Sample every 100ms
                val amplitude = getAmplitude()
                withContext(singleThreadDispatcher) {
                    amplitudes.add(amplitude)
                }
            }
        }
    }

    private fun getAmplitude(): Float {
        return if (isRecording && !isPaused) {
            try {
                audioRecorder?.updateMeters()
                val peakPower = audioRecorder?.peakPowerForChannel(0u) ?: -160.0

                // Convert dB to normalized amplitude (0.0 to 1.0)
                val dbValue = peakPower.toDouble()
                val maxValue = MAX_AMPLITUDE_VALUE
                val sum = dbValue + maxValue
                val result = sum / maxValue
                result.coerceIn(0.0, 1.0).toFloat()
            } catch (exception: Exception) {
                Logger.e(
                    tag = VoiceRecorderImp::class.simpleName ?: "",
                    messageString = "Error getting amplitude: ${exception.message}"
                )
                0f
            }
        } else {
            0f
        }
    }

    private fun startTrackingDuration() {
        durationJob = applicationScope.launch {
            var lastTime = NSDate().timeIntervalSince1970 * 1000

            while (isRecording && !isPaused) {
                delay(10L)
                val currentTime = NSDate().timeIntervalSince1970 * 1000
                val elapsedTime = currentTime - lastTime

                _recordingDetails.update {
                    it.copy(
                        duration = it.duration + elapsedTime.toLong().milliseconds
                    )
                }

                lastTime = currentTime
            }
        }
    }

    private fun generateTempFileUrl(): NSURL {
        val id = Uuid.random().toString()
        val fileName = "${TEMP_FILE_PREFIX}_$id.m4a"

        val documentsPath = NSFileManager.defaultManager.URLsForDirectory(
            NSDocumentDirectory,
            NSUserDomainMask
        ).firstOrNull() as? NSURL

        return documentsPath?.URLByAppendingPathComponent(fileName)
            ?: NSURL.fileURLWithPath(NSTemporaryDirectory() + fileName)
    }

    private fun setupAudioSession() {
        val audioSession = AVAudioSession.sharedInstance()
        memScoped {
            val error = alloc<ObjCObjectVar<NSError?>>()
            audioSession.setCategory(AVAudioSessionCategoryRecord, error.ptr)
            audioSession.setActive(true, error.ptr)
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
        Logger.d(
            tag = VoiceRecorderImp::class.simpleName ?: "",
            messageString = "Cleaning up voice recorder"
        )
        audioRecorder = null
        isRecording = false
        isPaused = false
        durationJob?.cancel()
        amplitudeJob?.cancel()
    }
}
