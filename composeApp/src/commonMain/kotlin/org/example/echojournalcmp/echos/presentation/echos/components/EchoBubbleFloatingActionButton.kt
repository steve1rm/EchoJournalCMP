package org.example.echojournalcmp.echos.presentation.echos.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.echojournalcmp.core.presentation.designsystem.theme.buttonGradient
import org.example.echojournalcmp.core.presentation.designsystem.theme.buttonGradientPressed
import org.example.echojournalcmp.echos.presentation.echos.model.BubbleFloatingActionButtonColors
import org.example.echojournalcmp.echos.presentation.echos.model.rememberBubbleFloatingActionButtonColors

@Composable
fun EchoBubbleFloatingActionButton(
    showBubble: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    primaryButtonSize: Dp = 56.dp,
    colors: BubbleFloatingActionButtonColors = rememberBubbleFloatingActionButtonColors(),
    modifier: Modifier = Modifier
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = modifier
            .background(
                brush = if(showBubble) {
                    colors.outerCircle
                }
                else {
                    SolidColor(Color.Transparent)
                },
                shape = CircleShape
            )
            .padding(10.dp)
            .background(
                brush = if(isPressed) {
                    colors.primaryPressed
                }
                else {
                    SolidColor(Color.Transparent)
                },
                shape = CircleShape
            )
            .padding(16.dp)
            .background(
                brush = if(isPressed)
                    MaterialTheme.colorScheme.buttonGradientPressed
                else MaterialTheme.colorScheme.buttonGradient,
                shape = CircleShape
            )
            .size(primaryButtonSize)
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}