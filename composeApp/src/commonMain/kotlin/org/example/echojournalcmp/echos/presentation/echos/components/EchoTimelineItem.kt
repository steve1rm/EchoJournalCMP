@file:OptIn(ExperimentalTime::class)

package org.example.echojournalcmp.echos.presentation.echos.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.echojournalcmp.echos.presentation.echos.model.PlaybackState
import org.example.echojournalcmp.echos.presentation.echos.model.RelativePosition
import org.example.echojournalcmp.echos.presentation.echos.model.TrackSizeInfo
import org.example.echojournalcmp.echos.presentation.model.EchoUi
import org.example.echojournalcmp.echos.presentation.model.MoodUi
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

private val noVerticalLineAboveIconModifier = Modifier.padding(top = 14.dp)
private val noVerticalLineBelowIconModifier = Modifier.height(8.dp)

@Composable
fun EchoTimelineItem(
    echoUi: EchoUi,
    relativePosition: RelativePosition,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onTrackSizeAvailable: (trackSizeInfo: TrackSizeInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(),
            contentAlignment = Alignment.TopCenter
        ) {
            if(relativePosition != RelativePosition.SINGLE_ITEM) {
                VerticalDivider(
                    modifier = when(relativePosition) {
                        RelativePosition.FIRST -> noVerticalLineAboveIconModifier
                        RelativePosition.LAST -> noVerticalLineBelowIconModifier
                        RelativePosition.IN_BETWEEN -> Modifier
                        else -> {
                            Modifier
                        }
                    }
                )
            }

            Image(
                imageVector = vectorResource(echoUi.mood.iconSet.fill),
                contentDescription = echoUi.title,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(32.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        EchoCard(
            echoUi = echoUi,
            onTrackSizeAvailable = onTrackSizeAvailable,
            onPlayClick = onPlayClick,
            onPauseClick = onPauseClick,
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
    }
}

@Preview
@Composable
fun EchoTimelineItemPreview() {
    val echoUi = EchoUi(
        id = 1,
        title = "Test Echo",
        mood = MoodUi.STRESSED,
        recordedAt = Clock.System.now().toEpochMilliseconds(),
        note = "This is a test note.",
        topics = listOf("Test", "Preview"),
        amplitudes = List(100) { kotlin.random.Random.nextFloat() },
        playbackTotalDuration = 30.seconds,
        playbackCurrentDuration = 10.seconds,
        playbackState = PlaybackState.PLAYING
    )
    EchoTimelineItem(
        echoUi = echoUi,
        relativePosition = RelativePosition.IN_BETWEEN,
        onPlayClick = {},
        onPauseClick = {},
        onTrackSizeAvailable = {}
    )
}

