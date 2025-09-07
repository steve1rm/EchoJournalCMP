package org.example.echojournalcmp.echos.presentation.echos.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import echojournalcmp.composeapp.generated.resources.Res
import echojournalcmp.composeapp.generated.resources.confused_and_happy_mood_faces
import echojournalcmp.composeapp.generated.resources.desc_echos_empty
import echojournalcmp.composeapp.generated.resources.moods
import echojournalcmp.composeapp.generated.resources.title_echos_empty
import org.example.echojournalcmp.core.presentation.designsystem.theme.EchoJournalCMPTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EchosEmptyBackground(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            imageVector = vectorResource(Res.drawable.moods),
            contentDescription = stringResource(Res.string.confused_and_happy_mood_faces)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.title_echos_empty),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = stringResource(Res.string.desc_echos_empty),
            style = MaterialTheme.typography.bodyMedium,

        )
    }
}

@Preview
@Composable
fun EchosEmptyBackgroundPreview() {
    EchoJournalCMPTheme {
        EchosEmptyBackground()
    }
}
