package org.example.echojournalcmp.echos.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.example.echojournalcmp.echos.presentation.model.MoodUi
import org.jetbrains.compose.resources.vectorResource

@Composable
fun MoodItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    moodUi: MoodUi,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .width(64.dp)
            .clickable(
                indication = null,
                interactionSource = null,
                onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            modifier = Modifier
                .height(40.dp),
            contentDescription = moodUi.title.asString(),
            imageVector = if(isSelected)
                vectorResource(moodUi.iconSet.fill)
            else vectorResource(moodUi.iconSet.outline),
            contentScale = ContentScale.FillHeight
        )

        Text(
            text = moodUi.title.asString(),
            style = MaterialTheme.typography.labelMedium,
            color = if(isSelected)
                MaterialTheme.colorScheme.onSurface
            else
                MaterialTheme.colorScheme.outline
        )
    }
}
