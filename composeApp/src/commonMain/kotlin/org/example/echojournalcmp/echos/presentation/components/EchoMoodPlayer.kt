package org.example.echojournalcmp.echos.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.echojournalcmp.core.presentation.designsystem.theme.MoodPrimary25
import org.example.echojournalcmp.core.presentation.designsystem.theme.MoodPrimary35
import org.example.echojournalcmp.core.presentation.designsystem.theme.MoodPrimary80
import org.example.echojournalcmp.core.presentation.util.defaultShadow
import org.example.echojournalcmp.echos.presentation.echos.model.PlaybackState
import org.example.echojournalcmp.echos.presentation.echos.model.TrackSizeInfo
import org.example.echojournalcmp.echos.presentation.model.MoodUi
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun EchoMoodPlayer(
    moodUi: MoodUi,
    playbackState: PlaybackState,
    playerProgress: () -> Float,
    durationPlayed: Duration,
    totalPlaybackDuration: Duration,
    powerRatios: List<Float>,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onTrackSizeAvailable: (TrackSizeInfo) -> Unit,
    amplitudeBarWidth: Dp = 5.dp,
    amplitudeBarSpacing: Dp = 4.dp,
    modifier: Modifier = Modifier,
) {
    val iconTint = when (moodUi) {
        null -> MoodPrimary80
        else -> moodUi.colorSet.vivid
    }
    val trackFillColor = when (moodUi) {
        null -> MoodPrimary80
        else -> moodUi.colorSet.vivid
    }
    val backgroundColor = when (moodUi) {
        null -> MoodPrimary25
        else -> moodUi.colorSet.faded
    }
    val trackColor = when (moodUi) {
        null -> MoodPrimary35
        else -> moodUi.colorSet.desaturated
    }

    val formattedDurationText = remember(durationPlayed, totalPlaybackDuration) {
        "${durationPlayed.formatMMSS()}/${totalPlaybackDuration.formatMMSS()}"
    }

    Surface(
        modifier = modifier
            .defaultShadow(),
        shape = RoundedCornerShape(10.dp),
        color = backgroundColor
    ) {

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            EchoPlaybackButton(
                playbackState = playbackState,
                onPlayClick = onPlayClick,
                onPauseClick = onPauseClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = iconTint
                )
            )

            EchoPlayBar(
                powerRatios = powerRatios,
                playerProgress = playerProgress,
                amplitudeBarWidth = amplitudeBarWidth,
                amplitudeBarSpacing = amplitudeBarSpacing,
                trackColor = trackColor,
                trackFillColor = trackFillColor,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 10.dp)
                    .padding(horizontal = 8.dp)
                    .weight(1f),
            )

            Text(
                text = formattedDurationText,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(end = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun EchoMoodPlayerPreview() {
    val powerRatios = remember {
        (1..30).map {
            Random.nextFloat()
        }
    }

    val playerProgress = { 0.51f }

    EchoMoodPlayer(
        moodUi = MoodUi.STRESSED,
        playbackState = PlaybackState.PLAYING,
        playerProgress = playerProgress,
        durationPlayed = 125.seconds,
        totalPlaybackDuration = 250.seconds,
        powerRatios = powerRatios,
        onPlayClick = {},
        onPauseClick = {},
        modifier = Modifier,
        amplitudeBarWidth = 5.dp,
        amplitudeBarSpacing = 4.dp,
        onTrackSizeAvailable = {}
    )
}
