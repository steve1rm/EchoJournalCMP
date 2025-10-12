package org.example.echojournalcmp.echos.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.example.echojournalcmp.echos.presentation.model.MoodUi

@Composable
fun MoodSelectorRow(
    selectedMood: MoodUi?,
    onMoodClick: (MoodUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MoodUi.entries.forEach { moodUi ->
            MoodItem(
                moodUi = moodUi,
                isSelected = selectedMood == moodUi,
                onClick = {
                    onMoodClick(moodUi)
                }
            )
        }
    }
}