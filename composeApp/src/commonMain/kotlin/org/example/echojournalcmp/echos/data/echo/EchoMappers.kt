@file:OptIn(ExperimentalTime::class)

package org.example.echojournalcmp.echos.data.echo

import org.example.echojournalcmp.core.database.echo.EchoEntity
import org.example.echojournalcmp.core.database.echo_topic_relation.EchoWithTopics
import org.example.echojournalcmp.core.database.topic.TopicEntity
import org.example.echojournalcmp.echos.domain.echos.Echo
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun EchoWithTopics.toEcho(): Echo {
    return Echo(
        id = this.echo.echoId,
        mood = this.echo.mood,
        note = this.echo.note,
        title = this.echo.title,
        recordedAt = Instant.fromEpochMilliseconds(this.echo.recordedAt),
        topics = this.topics.toTopics(),
        audioFilePath = this.echo.audioFilePath,
        audioPlaybackLength = this.echo.audioPlaybackLength.milliseconds,
        audioAmplitudes = this.echo.audioAmplitudes
    )
}

fun List<TopicEntity>.toTopics(): List<String> {
    return this.map { topicEntity ->
        topicEntity.topic
    }
}

fun Echo.toEchoWithTopic(): EchoWithTopics {
    return EchoWithTopics(
        EchoEntity(
            echoId = this.id ?: 0,
            title = this.title,
            mood = this.mood,
            recordedAt = this.recordedAt.toEpochMilliseconds(),
            audioFilePath = this.audioFilePath,
            audioAmplitudes = this.audioAmplitudes,
            note = this.note,
            audioPlaybackLength = this.audioPlaybackLength.inWholeMilliseconds
        ),
        topics = this.topics.map { topic ->
            TopicEntity(topic)
        }
    )
}
