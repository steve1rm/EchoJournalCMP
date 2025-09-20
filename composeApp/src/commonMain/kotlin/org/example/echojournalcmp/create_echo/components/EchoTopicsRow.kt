package org.example.echojournalcmp.create_echo.components

import androidx.collection.mutableIntSetOf
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import echojournalcmp.composeapp.generated.resources.Res
import echojournalcmp.composeapp.generated.resources.clear_selection
import echojournalcmp.composeapp.generated.resources.close
import echojournalcmp.composeapp.generated.resources.create_entry
import echojournalcmp.composeapp.generated.resources.hashtag
import echojournalcmp.composeapp.generated.resources.topic
import org.example.echojournalcmp.core.presentation.designsystem.chips.HashtagChip
import org.example.echojournalcmp.core.presentation.designsystem.dropdowns.Selectable
import org.example.echojournalcmp.core.presentation.designsystem.dropdowns.SelectableDropdownOptionsMenu
import org.example.echojournalcmp.core.presentation.designsystem.dropdowns.SelectableOptionExtra
import org.example.echojournalcmp.core.presentation.designsystem.text_fields.TransparentHintTextField
import org.example.echojournalcmp.screenHeight
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.vectorResource

@Composable
fun EchoTopicsRow(
    topics: List<String>,
    addTopicText: String,
    showCreateTopicOption: Boolean,
    showTopicSuggestions: Boolean,
    searchResults: List<Selectable<String>>,
    onTopicClick: (String) -> Unit,
    onDismissTopicSuggestions: () -> Unit,
    onRemoveTopicClick: (topic: String) -> Unit,
    onAddTopicTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    dropDownMaxHeight: Dp = (screenHeight * 0.3).dp // 30% of screen size
) {
    var topicRowHeight by remember {
        mutableIntStateOf(0)
    }

    var chipHeight by remember {
        mutableIntStateOf(0)
    }

    Row(
        modifier = modifier
            .onSizeChanged { size ->
                topicRowHeight = size.height
            },
    ) {
        Icon(
            modifier = Modifier
                .defaultMinSize(
                minHeight = with(LocalDensity.current) {
                    chipHeight.toDp()
                }
            ),
            imageVector = vectorResource(Res.drawable.hashtag),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outlineVariant
        )

        FlowRow(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            topics.forEach { topic ->
                HashtagChip(
                    modifier = Modifier
                        .onSizeChanged { size ->
                            chipHeight = size.height
                        },
                    text = topic,
                    trailingIcon = {
                        Icon(
                            modifier = Modifier
                                .size(14.dp)
                                .clickable {
                                    onRemoveTopicClick(topic)
                                },
                            imageVector = vectorResource(Res.drawable.close),
                            contentDescription = stringResource(Res.string.clear_selection),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }

            TransparentHintTextField(
                text = addTopicText,
                onValueChange = onAddTopicTextChange,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .defaultMinSize(minHeight = with(LocalDensity.current) {
                        chipHeight.toDp()
                    })
                    .fillMaxHeight(),
                maxLines = 1,
                hintText = if(topics.isEmpty())
                    stringResource(Res.string.topic)
                else
                    "Add here"
            )

            if(showTopicSuggestions) {
                SelectableDropdownOptionsMenu(
                    items = searchResults,
                    itemDisplayText = {
                        it
                    },
                    onDismiss = onDismissTopicSuggestions,
                    key = { it },
                    onItemClick = { onTopicClick(it.item) },
                    leadingIcon = {
                        Icon(
                            modifier = Modifier
                                .size(14.dp),
                            imageVector = vectorResource(Res.drawable.hashtag),
                            contentDescription = null
                        )
                    },
                    maxDropDownHeight = dropDownMaxHeight,
                    dropDownOffset = IntOffset(
                        x = 0,
                        y = topicRowHeight
                    ),
                    dropDownExtra =
                        if(showCreateTopicOption)
                            SelectableOptionExtra(
                                text = addTopicText,
                                onClick = {
                                    onTopicClick(addTopicText)
                                }
                            )
                        else
                            null
                )
            }
        }
    }
}

@Preview
@Composable
fun EchoTopicsRowPreview() {
    EchoTopicsRow(
        topics = listOf("topic1", "topic2"),
        addTopicText = "Add new topic",
        showCreateTopicOption = true,
        showTopicSuggestions = true,
        searchResults = listOf(
            Selectable("suggestion1", false),
            Selectable("suggestion2", true)
        ),
        onTopicClick = {},
        onDismissTopicSuggestions = {},
        onRemoveTopicClick = {},
        onAddTopicTextChange = {}
    )
}
