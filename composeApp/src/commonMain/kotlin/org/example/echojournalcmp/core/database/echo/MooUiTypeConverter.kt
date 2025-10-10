package org.example.echojournalcmp.core.database.echo

import androidx.room.TypeConverter
import org.example.echojournalcmp.echos.presentation.model.MoodUi

class MooUiTypeConverter {

    @TypeConverter
    fun fromMoodUi(moodUi: MoodUi): String {
        return moodUi.name
    }

    @TypeConverter
    fun toMoodUi(mood: String): MoodUi {
        return MoodUi.valueOf(mood)
    }
}
