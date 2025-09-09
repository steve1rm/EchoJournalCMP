@file:OptIn(ExperimentalTime::class)

package org.example.echojournalcmp.echos.presentation.echos.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.echojournalcmp.core.presentation.util.UiText
import org.example.echojournalcmp.echos.presentation.echos.model.EchoDaySection
import org.example.echojournalcmp.echos.presentation.echos.model.PlaybackState
import org.example.echojournalcmp.echos.presentation.echos.model.RelativePosition
import org.example.echojournalcmp.echos.presentation.echos.model.TrackSizeInfo
import org.example.echojournalcmp.echos.presentation.model.EchoUi
import org.example.echojournalcmp.echos.presentation.model.MoodUi
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime


@Composable
fun EchoList(
    sections: List<EchoDaySection>,
    onPlayClick: (echoId: Int) -> Unit,
    onPauseClick: () -> Unit,
    onTrackSizeAvailable: (TrackSizeInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {
        sections.forEachIndexed { sectionIndex, (dataHeader, echoes) ->
            stickyHeader {
                if(sectionIndex > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text(
                    text = dataHeader.asString().uppercase(),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            itemsIndexed(
                items = echoes,
                key = { _, echoUi ->
                    echoUi.id
                }
            ) { index, echoUi ->
                EchoTimelineItem(
                    echoUi = echoUi,
                    relativePosition = when {
                        index == 0 && echoes.size == 1 -> RelativePosition.SINGLE_ITEM
                        index == 0 -> RelativePosition.FIRST
                        echoes.lastIndex == index -> RelativePosition.LAST
                        else -> RelativePosition.IN_BETWEEN
                    },
                    onTrackSizeAvailable = onTrackSizeAvailable,
                    onPauseClick = onPauseClick,
                    onPlayClick = {
                        onPlayClick(echoUi.id)
                    })
            }
        }
    }
}


@Preview
@Composable
fun EchoListPreview() {
    val sections = listOf(
        EchoDaySection(
            dataHeader = UiText.Dynamic("Today"),
            echos = listOf(
                EchoUi(
                    id = 1,
                    title = "My First Echo",
                    mood = MoodUi.STRESSED,
                    recordedAt = Clock.System.now().toEpochMilliseconds(),
                    note = "This is a note for my first echo.",
                    topics = listOf("Work", "Productivity"),
                    amplitudes = listOf(0.1f, 0.2f, 0.3f, 0.2f, 0.1f),
                    playbackTotalDuration = 60.seconds,
                    playbackCurrentDuration = 30.seconds,
                    playbackState = PlaybackState.PLAYING
                ),
                EchoUi(
                    id = 2,
                    title = "My First Echo",
                    mood = MoodUi.SAD,
                    recordedAt = Clock.System.now().toEpochMilliseconds(),
                    note = "This is a note for my first echo.",
                    topics = listOf("Work", "Productivity"),
                    amplitudes = listOf(0.1f, 0.2f, 0.3f, 0.2f, 0.1f),
                    playbackTotalDuration = 60.seconds,
                    playbackCurrentDuration = 30.seconds,
                    playbackState = PlaybackState.PLAYING
                ),
                EchoUi(
                    id = 3,
                    title = "My First Echo",
                    mood = MoodUi.NEUTRAL,
                    recordedAt = Clock.System.now().toEpochMilliseconds(),
                    note = "This is a note for my first echo.",
                    topics = listOf("Work", "Productivity"),
                    amplitudes = listOf(0.1f, 0.2f, 0.3f, 0.2f, 0.1f),
                    playbackTotalDuration = 60.seconds,
                    playbackCurrentDuration = 30.seconds,
                    playbackState = PlaybackState.PLAYING
                )
            )
        )
    )
    EchoList(
        sections = sections,
        onPlayClick = {},
        onPauseClick = {},
        onTrackSizeAvailable = {}
    )
}



