package org.example.echojournalcmp.echos.presentation.settings.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import echojournalcmp.composeapp.generated.resources.Res
import echojournalcmp.composeapp.generated.resources.add
import echojournalcmp.composeapp.generated.resources.add_new_entry
import echojournalcmp.composeapp.generated.resources.close
import echojournalcmp.composeapp.generated.resources.my_topics
import echojournalcmp.composeapp.generated.resources.remove_topic
import echojournalcmp.composeapp.generated.resources.select_default_topics_to_apply_to_all_new_entries
import org.example.echojournalcmp.core.presentation.designsystem.chips.HashtagChip
import org.example.echojournalcmp.core.presentation.designsystem.dropdowns.Selectable.Companion.asUnselectedItems
import org.example.echojournalcmp.core.presentation.designsystem.dropdowns.SelectableDropdownOptionsMenu
import org.example.echojournalcmp.core.presentation.designsystem.dropdowns.SelectableOptionExtra
import org.example.echojournalcmp.core.presentation.designsystem.text_fields.TransparentHintTextField
import org.example.echojournalcmp.core.presentation.designsystem.theme.Gray6
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun DefaultTopicSelectorCard(
    topics: List<String>,
    searchText: String,
    topicSuggestions: List<String>,
    showCreateTopicOption: Boolean,
    showSuggestionsDropDown: Boolean,
    canInputText: Boolean,
    onSearchTextChange: (text: String) -> Unit,
    onTableCanInputText: () -> Unit,
    onAddTopicClick: (topic: String) -> Unit,
    onRemoveTopicClick: (topic: String) -> Unit,
    onDismissSuggestionsDropDown: () -> Unit,
    modifier: Modifier = Modifier
) {
    var topicSuggestionsVerticalOffset by remember {
        mutableIntStateOf(0)
    }

    val unselectedSuggestions = remember(topicSuggestions) {
        topicSuggestions.asUnselectedItems()
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(14.dp)
            .animateContentSize()
            ) {
        Text(
            text = stringResource(Res.string.my_topics),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = stringResource(Res.string.select_default_topics_to_apply_to_all_new_entries),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.onSizeChanged {
                topicSuggestionsVerticalOffset = it.height
            }
        ) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                topics.forEach { topic ->
                    HashtagChip(
                        text = topic,
                        trailingIcon = {
                            Icon(
                                imageVector = vectorResource(Res.drawable.close),
                                contentDescription = stringResource(Res.string.remove_topic),
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable {
                                        onRemoveTopicClick(topic)
                                    }
                            )
                        }
                    )
                }

                if(canInputText) {
                    TransparentHintTextField(
                        text = searchText,
                        onValueChange =  onSearchTextChange,
                        hintText = null,
                        textStyle = LocalTextStyle.current.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        maxLines = 1,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.None
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    )
                }
                else {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Gray6)
                            .clickable(onClick = onTableCanInputText),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.add),
                            contentDescription = stringResource(Res.string.add_new_entry),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .padding(2.dp)
                        )
                    }
                }
            }

            if(showSuggestionsDropDown) {
                SelectableDropdownOptionsMenu(
                    items = unselectedSuggestions,
                    itemDisplayText = { it },
                    onDismiss = onDismissSuggestionsDropDown,
                    key = { it },
                    onItemClick = {
                        onAddTopicClick(it.item)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = vectorResource(Res.drawable.close),
                            contentDescription = "Topics"
                        )
                    },
                    dropDownOffset = IntOffset(
                        x = 0,
                        y = topicSuggestionsVerticalOffset),
                    dropDownExtra = if(showCreateTopicOption) {
                        SelectableOptionExtra(
                            text = searchText,
                            onClick = {  }
                        )
                    } else {
                        null
                    }
                )
            }
        }
    }
}