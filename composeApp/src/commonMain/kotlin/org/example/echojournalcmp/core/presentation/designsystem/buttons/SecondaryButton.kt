package org.example.echojournalcmp.core.presentation.designsystem.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import org.example.echojournalcmp.core.presentation.designsystem.theme.EchoJournalCMPTheme
import org.example.echojournalcmp.core.presentation.designsystem.theme.buttonGradient
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            contentColor = MaterialTheme.colorScheme.primary
        ),

    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview
@Composable
fun SecondaryButtonPreview() {
    EchoJournalCMPTheme {
        SecondaryButton(
            text = "Secondary Button",
            onClick = {},
        )
    }
}