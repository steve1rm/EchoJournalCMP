package org.example.echojournalcmp.echos.presentation.echos.model

import echojournalcmp.composeapp.generated.resources.Res
import echojournalcmp.composeapp.generated.resources.all_moods
import org.example.echojournalcmp.core.presentation.util.UiText
import org.jetbrains.compose.resources.DrawableResource

data class MoodChipContent(
    val iconRes: List<DrawableResource> = emptyList(),
    val title: UiText = UiText.LocalizedString(Res.string.all_moods)
)
