package org.example.echojournalcmp.echos.presentation.echos.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import echojournalcmp.composeapp.generated.resources.Res
import echojournalcmp.composeapp.generated.resources.add
import echojournalcmp.composeapp.generated.resources.recording_your_memories
import org.example.echojournalcmp.core.presentation.designsystem.theme.EchoJournalCMPTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EchoRecordFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        content = {
            Icon(
                imageVector = vectorResource(Res.drawable.add),
                contentDescription = stringResource(Res.string.recording_your_memories)
            )
        }
    )
}

@Preview
@Composable
fun EchoRecordFloatingActionButtonPreview() {
    EchoJournalCMPTheme {
        EchoRecordFloatingActionButton(onClick = {})
    }
}
