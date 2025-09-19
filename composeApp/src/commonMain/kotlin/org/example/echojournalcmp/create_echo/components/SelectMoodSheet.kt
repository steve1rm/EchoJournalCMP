@file:OptIn(ExperimentalMaterial3Api::class)

package org.example.echojournalcmp.create_echo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import echojournalcmp.composeapp.generated.resources.Res
import echojournalcmp.composeapp.generated.resources.cancel
import echojournalcmp.composeapp.generated.resources.check
import echojournalcmp.composeapp.generated.resources.confirm
import echojournalcmp.composeapp.generated.resources.how_are_you_doing
import echojournalcmp.composeapp.generated.resources.save
import org.example.echojournalcmp.core.presentation.designsystem.buttons.PrimaryButton
import org.example.echojournalcmp.core.presentation.designsystem.buttons.SecondaryButton
import org.example.echojournalcmp.core.presentation.designsystem.theme.EchoJournalCMPTheme
import org.example.echojournalcmp.echos.presentation.model.MoodUi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SelectMoodSheet(
    modifier: Modifier = Modifier,
    selectedMood: MoodUi,
    onMoodClick: (MoodUi) -> Unit,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit
) {
    val allMoods = MoodUi.entries.toList()

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            Text(
                text = stringResource(Res.string.how_are_you_doing),
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                allMoods.forEach { moodUi ->
                    MoodItem(
                        moodUi = moodUi,
                        isSelected = selectedMood == moodUi,
                        onClick = {
                            onMoodClick(moodUi)
                        }
                    )
                }
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
                    onClick = onDismiss
                )

                PrimaryButton(
                    modifier = Modifier
                        .weight(1f),
                    text = stringResource(Res.string.confirm),
                    onClick = {
                        println("Should click here")
                        onConfirmClick()
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
}

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

@Preview
@Composable
fun SelectMoodSheetPreview() {
    EchoJournalCMPTheme {
        SelectMoodSheet(
            selectedMood = MoodUi.NEUTRAL,
            onMoodClick = {},
            onDismiss = {},
            onConfirmClick = {}
        )
    }
}

