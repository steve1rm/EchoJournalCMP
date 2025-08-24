package org.example.echojournalcmp.core.presentation.designsystem.chips

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.echojournalcmp.core.presentation.designsystem.theme.EchoJournalCMPTheme
import org.example.echojournalcmp.core.presentation.designsystem.theme.Gray6
import org.example.echojournalcmp.core.presentation.designsystem.theme.Pause
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HashtagChip(
    text: String,
    modifier: Modifier = Modifier,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    Surface(
        modifier = modifier,
        shape = CircleShape
    ) {
        Row(
            modifier = Modifier
                .background(color = Gray6)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
        ) {
            Text(
                text = "#"
            )

            Text(
                text = text
            )

            trailingIcon?.invoke()
        }
    }
}

@Composable
@Preview
fun HashtagChipPreview() {
    EchoJournalCMPTheme {
        HashtagChip(
            text = "Hashtag Chip",
            trailingIcon = {
                Icon(
                    imageVector = Pause,
                    contentDescription = null
                )
            }
        )
    }
}