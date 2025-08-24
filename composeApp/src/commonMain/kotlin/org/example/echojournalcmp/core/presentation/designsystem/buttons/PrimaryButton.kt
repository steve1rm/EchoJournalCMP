package org.example.echojournalcmp.core.presentation.designsystem.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import org.example.echojournalcmp.core.presentation.designsystem.theme.EchoJournalCMPTheme
import org.example.echojournalcmp.core.presentation.designsystem.theme.Microphone
import org.example.echojournalcmp.core.presentation.designsystem.theme.buttonGradient
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null
) {

    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = if(enabled) {
                MaterialTheme.colorScheme.onPrimary
            }
            else {
                MaterialTheme.colorScheme.outline
            }
        ),
        modifier = modifier.background(
            brush = if(enabled) {
                MaterialTheme.colorScheme.buttonGradient
            }
            else {
                SolidColor(MaterialTheme.colorScheme.surfaceVariant)
            },
            shape = CircleShape
        )
    ) {
        leadingIcon?.invoke()

        if(leadingIcon != null) {
            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = if(enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline
        )
    }
}

@Preview
@Composable
fun PrimaryButtonPreview() {
    EchoJournalCMPTheme {
        PrimaryButton(
            text = "Primary Button",
            enabled = false,
            onClick = {},
            leadingIcon = {
                Icon(imageVector = Microphone, contentDescription = null)
            }
        )
    }
}