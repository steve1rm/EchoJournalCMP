package org.example.echojournalcmp.echos.domain.recording

interface RecordingStorage {
    suspend fun savePersistently(tempFilePath: String): String?
    suspend fun cleanUpTemporaryFiles()

    companion object {
        const val RECORDING_FILE_EXTENSION = "mp4"
        const val TEMP_FILE_PREFIX = "temp_file_prefix"
        const val PERSISTENT_FILE_PREFIX = "recording"
    }
}