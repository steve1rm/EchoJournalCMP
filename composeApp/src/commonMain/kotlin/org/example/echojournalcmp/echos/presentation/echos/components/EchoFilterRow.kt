@file:OptIn(ExperimentalUuidApi::class)

package org.example.echojournalcmp.echos.presentation.echos.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import echojournalcmp.composeapp.generated.resources.Res
import echojournalcmp.composeapp.generated.resources.hashtag
import echojournalcmp.composeapp.generated.resources.you_don_t_have_any_topics_yet
import org.example.echojournalcmp.core.presentation.designsystem.chips.MultiChoiceChip
import org.example.echojournalcmp.core.presentation.designsystem.dropdowns.Selectable
import org.example.echojournalcmp.core.presentation.designsystem.dropdowns.SelectableDropdownOptionsMenu
import org.example.echojournalcmp.echos.presentation.model.MoodUi
import org.example.echojournalcmp.echos.presentation.echos.EchosAction
import org.example.echojournalcmp.echos.presentation.echos.model.EchoFilterChip
import org.example.echojournalcmp.echos.presentation.echos.model.MoodChipContent
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun EchoFilterRow(
    moodChipContent: MoodChipContent,
    hasActiveMoodFilters: Boolean,
    selectedEchoFilterChip: EchoFilterChip?,
    moods: List<Selectable<MoodUi>>,
    hasActiveTopicFilters: Boolean,
    topicChipTitle: String,
    topics: List<Selectable<String>>,
    onAction: (EchosAction) -> Unit,
    modifier: Modifier = Modifier
) {

    var dropDownOffset by remember {
        mutableStateOf(IntOffset.Zero)
    }

    FlowRow(
        modifier = modifier
            .padding(16.dp)
            .onGloballyPositioned { coordinates ->
                dropDownOffset = IntOffset(
                    x = 0,
                    y = coordinates.size.height)
            },
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        MultiChoiceChip(
            displayText = moodChipContent.title.asString(),
            onClick = {
                onAction(EchosAction.OnMoodChipClick)
            },
            leadingContent = {
                if(moodChipContent.iconRes.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy((-4).dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        moodChipContent.iconRes.forEach { iconRes ->
                            Image(
                                modifier = Modifier.height(16.dp),
                                imageVector = vectorResource(iconRes),
                                contentDescription = moodChipContent.title.asString()
                            )
                        }
                    }
                }
            },
            isClearVisible = hasActiveMoodFilters,
            isDropDownVisible = selectedEchoFilterChip == EchoFilterChip.MOODS,
            isHighlighted = hasActiveMoodFilters || selectedEchoFilterChip == EchoFilterChip.MOODS,
            onClearButtonClick = {
                onAction(EchosAction.OnRemoveFilters(EchoFilterChip.MOODS))
            },
            dropDownMenu = {
                SelectableDropdownOptionsMenu(
                    items = moods,
                    itemDisplayText = { moodUi ->
                        moodUi.title.asString()
                    },
                    onDismiss = {
                        onAction(EchosAction.OnDismissMoodDropDown)
                    },
                    key = { moodUi ->
                        moodUi.title.hashCode()
                    },
                    onItemClick = { moodUi ->
                        onAction(EchosAction.OnFilterByMoodClick(moodUi.item))
                    },
                    dropDownOffset = dropDownOffset,
                    leadingIcon = { moodUi ->
                        Image(
                            modifier = Modifier.size(16.dp),
                            imageVector = vectorResource(moodUi.iconSet.fill),
                            contentDescription = moodUi.title.asString()
                        )
                    }
                )
            },
        )

        MultiChoiceChip(
            displayText = topicChipTitle,
            onClick = {
                onAction(EchosAction.OnTopicChipClick)
            },
            isClearVisible = hasActiveTopicFilters,
            isDropDownVisible = selectedEchoFilterChip == EchoFilterChip.TOPICS,
            isHighlighted = hasActiveTopicFilters || selectedEchoFilterChip == EchoFilterChip.TOPICS,
            onClearButtonClick = {
                onAction(EchosAction.OnRemoveFilters(EchoFilterChip.TOPICS))
            },
            dropDownMenu = {
                if(topics.isEmpty()) {
                    SelectableDropdownOptionsMenu(
                        items = listOf(Selectable(
                            item = stringResource(Res.string.you_don_t_have_any_topics_yet),
                            selected = false
                        )),
                        itemDisplayText = { it },
                        onDismiss = {
                            onAction(EchosAction.OnDismissTopicDropDown)
                        },
                        key = {topic -> topic},
                        onItemClick = {},
                        dropDownOffset = dropDownOffset,
                    )
                }
                else {
                    SelectableDropdownOptionsMenu(
                        items = topics,
                        itemDisplayText = { topic ->
                            topic
                        },
                        onDismiss = {
                            onAction(EchosAction.OnDismissTopicDropDown)
                        },
                        key = { topic ->
                            topic
                        },
                        onItemClick = { topic ->
                            onAction(EchosAction.OnFilterByTopicClick(topic.item))
                        },
                        dropDownOffset = dropDownOffset,
                        leadingIcon = { topic ->
                            Image(
                                modifier = Modifier.size(16.dp),
                                imageVector = vectorResource(Res.drawable.hashtag),
                                contentDescription = topic
                            )
                        }
                    )
                }
            },
        )
    }
}