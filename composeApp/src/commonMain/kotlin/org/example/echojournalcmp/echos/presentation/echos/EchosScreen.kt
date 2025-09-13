package org.example.echojournalcmp.echos.presentation.echos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.example.echojournalcmp.core.presentation.designsystem.theme.EchoJournalCMPTheme
import org.example.echojournalcmp.core.presentation.designsystem.theme.bgGradient
import org.example.echojournalcmp.echos.presentation.components.EchoRecordingSheet
import org.example.echojournalcmp.echos.presentation.echos.components.EchoFilterRow
import org.example.echojournalcmp.echos.presentation.echos.components.EchoList
import org.example.echojournalcmp.echos.presentation.echos.components.EchoRecordFloatingActionButton
import org.example.echojournalcmp.echos.presentation.echos.components.EchosEmptyBackground
import org.example.echojournalcmp.echos.presentation.echos.components.EchosTopBar
import org.example.echojournalcmp.echos.presentation.echos.model.RecordingState
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun EchosScreen(
    state: EchosState,
    onAction: (EchosAction) -> Unit,
) {
    Scaffold(
        topBar = {
            EchosTopBar(
                onSettingsClick = {
                    onAction(EchosAction.OnSettingsClick)
                }
            )
        },
        floatingActionButton = {
            EchoRecordFloatingActionButton(
                onClick = {
                    onAction(EchosAction.OnFabClick)
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = MaterialTheme.colorScheme.bgGradient
                    )
                    .padding(innerPadding)
            ) {
                EchoFilterRow(
                    moodChipContent = state.moodChipContent,
                    hasActiveMoodFilters = state.hasActiveMoodFilters,
                    selectedEchoFilterChip = state.selectedEchoFilterChip,
                    moods = state.moods,
                    topicChipTitle = state.topicChipTitle.asString(),
                    hasActiveTopicFilters = state.hasActiveTopicFilters,
                    topics = state.topics,
                    onAction = onAction,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                when {
                    state.isLoadingData -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .wrapContentSize(),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    !state.hasEchosRecorded -> {
                        EchosEmptyBackground(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        )
                    }
                    else -> {
                        EchoList(
                            sections = state.echoDaySections,
                            onPlayClick = {
                                onAction(EchosAction.OnPlayEchoClick(it))
                            },
                            onPauseClick = {
                                onAction(EchosAction.OnPausedRecordingClick)
                            },
                            onTrackSizeAvailable = { trackSize ->
                                onAction(EchosAction.OnTrackSizeAvailable(trackSize))
                            }
                        )
                    }
                }
            }

            if(state.recordingState in listOf(RecordingState.NORMAL_CAPTURE, RecordingState.PAUSED)) {
                EchoRecordingSheet(
                    formattedRecordDuration = state.formattedRecordDuration,
                    isRecording = state.recordingState == RecordingState.NORMAL_CAPTURE,
                    onDismiss = {
                        onAction(EchosAction.OnCancelRecording)
                    },
                    onPauseClick = {
                        onAction(EchosAction.OnPausedRecordingClick)
                    },
                    onResumeClick = {
                        onAction(EchosAction.OnResumedRecordingClick)
                    },
                    onCompleteRecording = {
                        onAction(EchosAction.OnCompletedRecording)
                    }
                )
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    EchoJournalCMPTheme {
        EchosScreen(
            state = EchosState(
                isLoadingData = false,
                hasEchosRecorded = false
            ),
            onAction = {}
        )
    }
}