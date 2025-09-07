package org.example.echojournalcmp.echos.presentation.components

import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import echojournalcmp.composeapp.generated.resources.Res
import echojournalcmp.composeapp.generated.resources.pause
import echojournalcmp.composeapp.generated.resources.paused
import echojournalcmp.composeapp.generated.resources.play
import echojournalcmp.composeapp.generated.resources.playing
import echojournalcmp.composeapp.generated.resources.stopped
import org.example.echojournalcmp.core.presentation.util.defaultShadow
import org.example.echojournalcmp.echos.presentation.PlaybackState
import org.example.echojournalcmp.echos.presentation.model.MoodUi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EchoPlaybackButton(
    playbackState: PlaybackState,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    colors: IconButtonColors,
    modifier: Modifier = Modifier
) {
    FilledIconButton(
        modifier = modifier
            .defaultShadow(),
        onClick = when (playbackState) {
            PlaybackState.PLAYING -> onPauseClick
            PlaybackState.PAUSED,
            PlaybackState.STOPPED -> onPlayClick
        },
        colors = colors
    ) {
        Icon(
            imageVector = when(playbackState) {
                PlaybackState.PLAYING -> vectorResource(Res.drawable.pause)
                PlaybackState.PAUSED,
                PlaybackState.STOPPED -> vectorResource(Res.drawable.play)
            },
            contentDescription = when(playbackState) {
                PlaybackState.PLAYING -> stringResource(Res.string.playing)
                PlaybackState.PAUSED -> stringResource(Res.string.paused)
                PlaybackState.STOPPED -> stringResource(Res.string.stopped)
            }
        )
    }
}

@Preview
@Composable
fun EchoPlaybackButtonPreview() {
    EchoPlaybackButton(
        playbackState = PlaybackState.PLAYING,
        onPlayClick = {},
        onPauseClick = {},
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MoodUi.STRESSED.colorSet.vivid
        ),
        modifier = Modifier
    )
}
