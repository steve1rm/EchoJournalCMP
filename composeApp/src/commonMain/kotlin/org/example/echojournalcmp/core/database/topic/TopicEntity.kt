package org.example.echojournalcmp.core.database.topic

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TopicEntity(
    @PrimaryKey(autoGenerate = false)
    val topic: String
)