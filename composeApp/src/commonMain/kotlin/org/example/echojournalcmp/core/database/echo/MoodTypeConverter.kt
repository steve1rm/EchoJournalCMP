package org.example.echojournalcmp.core.database.echo

import androidx.room.TypeConverter
import org.example.echojournalcmp.echos.domain.echos.Mood

class MoodTypeConverter {

    @TypeConverter
    fun fromMoodUi(moodUi: Mood): String {
        return moodUi.name
    }

    @TypeConverter
    fun toMoodUi(mood: String): Mood {
        return Mood.valueOf(mood)
    }
}
