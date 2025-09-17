@file:OptIn(ExperimentalMaterial3Api::class)

package org.example.echojournalcmp.echos.presentation.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import echojournalcmp.composeapp.generated.resources.Res
import echojournalcmp.composeapp.generated.resources.cancel
import echojournalcmp.composeapp.generated.resources.check
import echojournalcmp.composeapp.generated.resources.close
import echojournalcmp.composeapp.generated.resources.finish_recording
import echojournalcmp.composeapp.generated.resources.mic
import echojournalcmp.composeapp.generated.resources.pause
import echojournalcmp.composeapp.generated.resources.paused
import echojournalcmp.composeapp.generated.resources.recording_your_memories
import echojournalcmp.composeapp.generated.resources.resume_recording
import org.example.echojournalcmp.core.presentation.designsystem.theme.EchoJournalCMPTheme
import org.example.echojournalcmp.core.presentation.designsystem.theme.buttonGradient
import org.example.echojournalcmp.core.presentation.designsystem.theme.buttonGradientPressed
import org.example.echojournalcmp.core.presentation.designsystem.theme.primary90
import org.example.echojournalcmp.core.presentation.designsystem.theme.primary95
import org.example.echojournalcmp.echos.presentation.echos.components.EchoBubbleFloatingActionButton
import org.example.echojournalcmp.echos.presentation.echos.model.BubbleFloatingActionButtonColors
import org.example.echojournalcmp.echos.presentation.echos.model.rememberBubbleFloatingActionButtonColors
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val PRIMARY_BUTTON_BUBBLE_SIZE_DP = 128
private const val SECONDARY_BUTTON_SIZE_DP = 48

@Composable
fun EchoRecordingSheet(
    formattedRecordDuration: String,
    isRecording: Boolean,
    onDismiss: () -> Unit,
    onResumeClick: () -> Unit,
    onPauseClick: () -> Unit,
    onCompleteRecording: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        SheetContent(
            formattedRecordDuration = formattedRecordDuration,
            isRecording = isRecording,
            onDismiss = onDismiss,
            onResumeClick = onResumeClick,
            onPauseClick = onPauseClick,
            onCompleteRecording = onCompleteRecording,
            modifier = modifier
        )
    }
}

@Composable
fun SheetContent(
    formattedRecordDuration: String,
    isRecording: Boolean,
    onDismiss: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onCompleteRecording: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryBubbleSize = PRIMARY_BUTTON_BUBBLE_SIZE_DP.dp
    val secondaryBubbleSize = SECONDARY_BUTTON_SIZE_DP.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text =  if(isRecording) {
                stringResource(Res.string.recording_your_memories)
            } else {
                stringResource(Res.string.paused)
            },
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.defaultMinSize(minWidth = 100.dp),
            text = formattedRecordDuration,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFeatureSettings = "tnum",
                textAlign = TextAlign.Center
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            FilledIconButton(
                modifier = Modifier
                    .size(secondaryBubbleSize),
                onClick = onDismiss,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.close),
                    contentDescription = stringResource(Res.string.cancel)
                )
            }

            EchoBubbleFloatingActionButton(
                showBubble = isRecording,
                onClick = if(isRecording) {
                    onCompleteRecording
                }
                else {
                    onResumeClick
                },
                icon = {
                    Icon(
                        imageVector = if(isRecording) {
                            vectorResource(Res.drawable.check)
                        } else vectorResource(Res.drawable.mic),
                        contentDescription = if(isRecording) {
                            stringResource(Res.string.finish_recording)
                        } else {
                            stringResource(Res.string.resume_recording)
                        },
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                primaryButtonSize = 72.dp
            )

            FilledIconButton(
                modifier = Modifier
                    .size(secondaryBubbleSize),
                onClick = if(isRecording) onPauseClick else onCompleteRecording,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = if(isRecording)
                        vectorResource(Res.drawable.pause)
                    else vectorResource(Res.drawable.check),
                    contentDescription = "Pause or resume",
                )
            }
        }
    }
}



@Preview
@Composable
fun EchoRecordingSheetPreview() {
    EchoJournalCMPTheme {
        EchoRecordingSheet(
            formattedRecordDuration = "00:00:00",
            isRecording = false,
            onDismiss = {},
            onResumeClick = {},
            onPauseClick = {},
            onCompleteRecording = {}
        )
    }
}

@Preview
@Composable
fun SheetContentPreview() {
    EchoJournalCMPTheme {
        SheetContent(
            formattedRecordDuration = "00:00:00",
            isRecording = false,
            onDismiss = {},
            onPauseClick = {},
            onResumeClick = {},
            onCompleteRecording = {}
        )
    }
}


