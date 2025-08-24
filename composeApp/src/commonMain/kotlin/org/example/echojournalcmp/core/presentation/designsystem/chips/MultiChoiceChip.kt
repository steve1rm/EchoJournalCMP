package org.example.echojournalcmp.core.presentation.designsystem.chips

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRowScope
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import echojournalcmp.composeapp.generated.resources.Res
import echojournalcmp.composeapp.generated.resources.ai
import echojournalcmp.composeapp.generated.resources.clear_selection
import echojournalcmp.composeapp.generated.resources.close
import echojournalcmp.composeapp.generated.resources.moods
import org.example.echojournalcmp.core.presentation.designsystem.theme.EchoJournalCMPTheme
import org.example.echojournalcmp.core.presentation.designsystem.theme.Gray6
import org.example.echojournalcmp.core.presentation.designsystem.theme.Pause
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MultiChoiceChip(
    displayText: String,
    onClick: () -> Unit,
    isClearVisible: Boolean,
    onClearButtonClick: () -> Unit,
    isHighlighted: Boolean,
    isDropDownVisible: Boolean,
    dropDownMenu: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    leadingContent: (@Composable () -> Unit)? = null
) {
    val containerColor = if(isHighlighted) {
        MaterialTheme.colorScheme.surface
    }
    else {
        Color.Transparent
    }

    val borderColor = if(isHighlighted) {
        MaterialTheme.colorScheme.primaryContainer
    }
    else {
        MaterialTheme.colorScheme.outline
    }

    Box(
        modifier = modifier
            .then(
                if(isHighlighted) {
                    Modifier.shadow(
                        elevation = 4.dp,
                        shape = CircleShape
                    )
                }
                else {
                    Modifier
                }
            )
            .clip(CircleShape)
            .border(
                width = 0.5.dp,
                color = borderColor,
                shape = CircleShape
            )
            .background(containerColor)
            .clickable(onClick = onClick)
            .animateContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Center)
        ) {
            leadingContent?.invoke()

            Text(
                text = displayText,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary
            )

            AnimatedVisibility(
                visible = isClearVisible
            ) {
                IconButton(
                    onClick = onClearButtonClick,
                    modifier = Modifier
                        .size(16.dp)
                ) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.close),
                        contentDescription = stringResource(resource = Res.string.clear_selection),
                        tint = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            }
        }

        if(isDropDownVisible) {
            dropDownMenu()
        }
    }
}

@Composable
@Preview
fun MultiChoiceChipPreview() {
    EchoJournalCMPTheme {
        MultiChoiceChip(
            displayText = "All topics",
            onClick = {},
            isClearVisible = true,
            onClearButtonClick = {},
            isHighlighted = false,
            isDropDownVisible = false,
            dropDownMenu = {},
            leadingContent = {
                Icon(
                    imageVector = vectorResource(Res.drawable.ai),
                    contentDescription = "Icons"
                )
            }
        )
    }
}