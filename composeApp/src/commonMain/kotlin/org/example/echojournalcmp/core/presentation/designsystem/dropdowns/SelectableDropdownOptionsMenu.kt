package org.example.echojournalcmp.core.presentation.designsystem.dropdowns

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import echojournalcmp.composeapp.generated.resources.Res
import echojournalcmp.composeapp.generated.resources.hashtag
import org.example.echojournalcmp.core.presentation.designsystem.dropdowns.Selectable.Companion.asUnselectedItems
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Suppress("EmptyFunctionBlock")
@Composable
fun <T> SelectableDropdownOptionsMenu(
    items: List<Selectable<T>>,
    onDismiss: () -> Unit,
    key: (T) -> Any,
    onItemClick: () -> Unit,
    leadingIcon: (@Composable () -> Unit)? = null,
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
                    .padding(6.dp)
            ) {
                items(
                    items = items,
                    key = { selectable ->
                        key(selectable.item)
                    }
                ) {

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
            Selectable("Option 1", true),
            Selectable("Option 2", false)
        ).asUnselectedItems(),
        onDismiss = {},
        onItemClick = {},
        leadingIcon = {
            Icon(
                imageVector = vectorResource(Res.drawable.hashtag),
                contentDescription = null
            )
        },
        maxDropDownHeight = 500.dp,
        dropDownExtra = SelectableOptionExtra(
            "All topics",
            onClick = {}
        )
    )
}
