package org.example.echojournalcmp.core.presentation.designsystem.dropdowns

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import echojournalcmp.composeapp.generated.resources.Res
import echojournalcmp.composeapp.generated.resources.add
import echojournalcmp.composeapp.generated.resources.check
import echojournalcmp.composeapp.generated.resources.create_entry
import echojournalcmp.composeapp.generated.resources.hashtag
import org.example.echojournalcmp.core.presentation.designsystem.dropdowns.Selectable.Companion.asUnselectedItems
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Suppress("EmptyFunctionBlock")
@Composable
fun <T> SelectableDropdownOptionsMenu(
    items: List<Selectable<T>>,
    onDismiss: () -> Unit,
    key: (T) -> Any,
    onItemClick: (selectable: Selectable<T>) -> Unit,
    itemDisplayText: @Composable (item: T) -> String,
    leadingIcon: (@Composable (item: T) -> Unit)? = null,
    dropDownOffset: IntOffset = IntOffset.Zero,
    maxDropDownHeight: Dp = Dp.Unspecified,
    dropDownExtra: SelectableOptionExtra? = null,
    modifier: Modifier = Modifier
) {

    Popup(
        onDismissRequest = onDismiss,
        offset = dropDownOffset
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(10.dp),
            shadowElevation = 4.dp,
            modifier = modifier
                .heightIn(max = maxDropDownHeight)
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .animateContentSize()
                    .padding(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    items = items,
                    key = { selectable ->
                        key(selectable.item)
                    }
                ) {selectable ->
                    Row(
                        modifier = Modifier
                            .animateItem()
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                color = if(selectable.selected) {
                                    MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.0f)
                                }
                                else {
                                    MaterialTheme.colorScheme.surface
                                }
                            )
                            .clickable {
                                onItemClick(selectable)
                            }
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        leadingIcon?.invoke(selectable.item)

                        Text(
                            modifier = Modifier.weight(1f),
                            text = itemDisplayText(selectable.item),
                        )

                        if(selectable.selected) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.check),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                if(dropDownExtra != null && dropDownExtra.text.isNotEmpty()) {
                    item(key = true) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable {
                                    dropDownExtra.onClick()
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(18.dp),
                                imageVector = vectorResource(Res.drawable.add),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                modifier = Modifier.weight(1f),
                                text = "${stringResource(Res.string.create_entry)} ${dropDownExtra.text}",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SelectableDropdownOptionsMenuPreview() {
    SelectableDropdownOptionsMenu(
        items = listOf(
            Selectable(
                item = "Hello world 1",
                selected = false
            ),
            Selectable(
                item = "Hello world 2",
                selected = true
            ),
            Selectable(
                item = "Hello world 3",
                selected = true
            ),
        ),
        key = { it },
        itemDisplayText = { it },
        onDismiss = {},
        onItemClick = {},
        leadingIcon = {
            Icon(
                imageVector = vectorResource(Res.drawable.hashtag),
                contentDescription = null
            )
        },
        maxDropDownHeight = 500.dp,
    )
}
