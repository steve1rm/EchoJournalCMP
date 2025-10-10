package org.example.echojournalcmp.core.database.echo

import androidx.room.TypeConverter

class FloatListTypeConverter {

    @TypeConverter
    fun fromList(values: List<Float>): String {
        return values.joinToString(",")
    }

    @TypeConverter
    fun toList(values: String): List<Float> {
        return values
            .split(",")
            .map { amplitude -> amplitude.toFloat() }
    }
}