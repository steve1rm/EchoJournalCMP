package org.example.echojournalcmp.core.presentation.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import echojournalcmp.composeapp.generated.resources.Res
import echojournalcmp.composeapp.generated.resources.microphone
import echojournalcmp.composeapp.generated.resources.pause
import org.jetbrains.compose.resources.vectorResource


val Pause: ImageVector
    @Composable
    get() {
        return vectorResource(Res.drawable.pause)
    }

val Microphone: ImageVector
    @Composable
    get() {
        return vectorResource(resource = Res.drawable.microphone)
    }