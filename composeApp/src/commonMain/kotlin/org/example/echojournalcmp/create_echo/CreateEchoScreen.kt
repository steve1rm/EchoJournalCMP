@file:OptIn(ExperimentalMaterial3Api::class)

package org.example.echojournalcmp.create_echo

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import echojournalcmp.composeapp.generated.resources.Res
import echojournalcmp.composeapp.generated.resources.add
import echojournalcmp.composeapp.generated.resources.add_description
import echojournalcmp.composeapp.generated.resources.add_mood
import echojournalcmp.composeapp.generated.resources.add_title
import echojournalcmp.composeapp.generated.resources.back
import echojournalcmp.composeapp.generated.resources.cancel
import echojournalcmp.composeapp.generated.resources.check
import echojournalcmp.composeapp.generated.resources.edit
import echojournalcmp.composeapp.generated.resources.navigate_back
import echojournalcmp.composeapp.generated.resources.new_entry
import echojournalcmp.composeapp.generated.resources.sad
import echojournalcmp.composeapp.generated.resources.save
import echojournalcmp.composeapp.generated.resources.selected_mood
import org.example.echojournalcmp.core.presentation.designsystem.buttons.PrimaryButton
import org.example.echojournalcmp.core.presentation.designsystem.buttons.SecondaryButton
import org.example.echojournalcmp.core.presentation.designsystem.text_fields.TransparentHintTextField
import org.example.echojournalcmp.core.presentation.designsystem.theme.EchoJournalCMPTheme
import org.example.echojournalcmp.core.presentation.designsystem.theme.secondary70
import org.example.echojournalcmp.core.presentation.designsystem.theme.secondary95
import org.example.echojournalcmp.echos.presentation.components.EchoMoodPlayer
import org.example.echojournalcmp.echos.presentation.model.MoodUi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CreateEchoScreen(
    state: CreateEchoState,
    onAction: (CreateEchoAction) -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(CreateEchoAction.OnNavigateBackClick)
                        }
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.back),
                            contentDescription = stringResource(Res.string.navigate_back)
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(Res.string.new_entry),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
            )
        },
        content = { innerPadding ->
            val descriptionFocusRequester = remember {
                FocusRequester()
            }

            val focusManager = LocalFocusManager.current


            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    if(state.mood == null) {
                        FilledIconButton(
                            onClick = {
                                onAction(CreateEchoAction.OnSelectMoodClick)
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.secondary95,
                                contentColor = MaterialTheme.colorScheme.secondary70
                            )
                        ) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.add),
                                contentDescription = stringResource(Res.string.add_mood)
                            )
                        }
                    }
                    else {
                        Image(
                            modifier = Modifier
                                .size(32.dp)
                                .clickable {
                                    onAction(CreateEchoAction.OnSelectMoodClick)
                                },
                            imageVector = vectorResource(state.mood.iconSet.fill),
                            contentDescription = state.mood.title.asString(),
                        )
                    }

                    TransparentHintTextField(
                        modifier = Modifier
                            .weight(1f),
                        text = state.titleText,
                        onValueChange = { title ->
                            onAction(CreateEchoAction.OnTitleTextChange(title))
                        },
                        hintText = stringResource(Res.string.add_title),
                        textStyle = MaterialTheme.typography.headlineLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                descriptionFocusRequester.requestFocus()
                            }
                        )
                    )
                }

                EchoMoodPlayer(
                    moodUi = state.selectedMood,
                    playbackState = state.playbackState,
                    playerProgress = { state.durationPlayedRatio },
                    durationPlayed = state.durationPlayed,
                    totalPlaybackDuration = state.playbackTotalDuration,
                    powerRatios = state.playbackAmplitudes,
                    onPlayClick = {
                        onAction(CreateEchoAction.OnPlayAudioClick)
                    },
                    onPauseClick = {
                        onAction(CreateEchoAction.OnPauseAudioClick)
                    },
                    onTrackSizeAvailable = { trackSizeInfo ->
                        onAction(CreateEchoAction.OnTrackSizeAvailable(trackSizeInfo))
                    }
                )

                // TODO insert topicsFlowRow

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = vectorResource(Res.drawable.edit),
                        contentDescription = stringResource(Res.string.add_description),
                        tint = MaterialTheme.colorScheme.outlineVariant
                    )

                    TransparentHintTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .focusRequester(descriptionFocusRequester),
                        text = state.noteText,
                        onValueChange = { description ->
                            onAction(CreateEchoAction.OnNoteTextChange(description))
                        },
                        hintText = stringResource(Res.string.add_description),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        )
                    )

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(intrinsicSize = IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SecondaryButton(
                        modifier = Modifier.fillMaxHeight(),
                        text = stringResource(Res.string.cancel),
                        onClick = {
                            onAction(CreateEchoAction.OnCancelClick)
                        }
                    )

                    PrimaryButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        text = stringResource(Res.string.save),
                        enabled = state.canSaveEcho,
                        onClick = {
                            onAction(CreateEchoAction.OnSaveClick)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = vectorResource(Res.drawable.check),
                                contentDescription = stringResource(Res.string.save)
                            )
                        }
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    EchoJournalCMPTheme {
        CreateEchoScreen(
            state = CreateEchoState(
                mood = MoodUi.PEACEFUL,
                canSaveEcho = true
            ),
            onAction = {}
        )
    }
}