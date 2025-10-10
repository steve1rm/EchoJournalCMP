package org.example.echojournalcmp.core.database.echo_topic_relation

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import org.example.echojournalcmp.core.database.echo.EchoEntity
import org.example.echojournalcmp.core.database.topic.TopicEntity

@Entity(
    primaryKeys = ["echoId", "topic"]
)
data class EchoTopicCrossRef(
    val echoId: Int,
    val topic: String
)

data class EchoWithTopics(
    // Get all the topics belonging to the echo
    @Embedded val echo: EchoEntity,
    @Relation(
        parentColumn = "echoId",
        entityColumn = "topic",
        associateBy = Junction(EchoTopicCrossRef::class)
    )
    val topics: List<TopicEntity>
)


