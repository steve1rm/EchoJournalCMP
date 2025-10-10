package org.example.echojournalcmp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.example.echojournalcmp.core.database.echo.EchoDao
import org.example.echojournalcmp.core.database.echo.EchoEntity
import org.example.echojournalcmp.core.database.echo.FloatListTypeConverter
import org.example.echojournalcmp.core.database.echo.MoodTypeConverter
import org.example.echojournalcmp.core.database.echo_topic_relation.EchoTopicCrossRef
import org.example.echojournalcmp.core.database.topic.TopicEntity

@Database(
    entities = [EchoEntity::class, TopicEntity::class, EchoTopicCrossRef::class],
    version = 1
)
@TypeConverters(
    MoodTypeConverter::class,
    FloatListTypeConverter::class
)
abstract class EchoDatabase : RoomDatabase() {
    abstract val echoDao: EchoDao
}
