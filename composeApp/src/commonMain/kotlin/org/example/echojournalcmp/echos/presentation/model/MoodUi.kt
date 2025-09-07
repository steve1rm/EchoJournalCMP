package org.example.echojournalcmp.echos.presentation.model

import androidx.compose.ui.graphics.Color
import echojournalcmp.composeapp.generated.resources.Res
import echojournalcmp.composeapp.generated.resources.emoji_excited
import echojournalcmp.composeapp.generated.resources.emoji_excited_outline
import echojournalcmp.composeapp.generated.resources.emoji_neutral
import echojournalcmp.composeapp.generated.resources.emoji_neutral_outline
import echojournalcmp.composeapp.generated.resources.emoji_peaceful
import echojournalcmp.composeapp.generated.resources.emoji_peaceful_outline
import echojournalcmp.composeapp.generated.resources.emoji_sad
import echojournalcmp.composeapp.generated.resources.emoji_stressed
import echojournalcmp.composeapp.generated.resources.emoji_stressed_outline
import echojournalcmp.composeapp.generated.resources.excited
import echojournalcmp.composeapp.generated.resources.neutral
import echojournalcmp.composeapp.generated.resources.peaceful
import echojournalcmp.composeapp.generated.resources.sad
import echojournalcmp.composeapp.generated.resources.stressed
import org.example.echojournalcmp.core.presentation.designsystem.theme.Excited25
import org.example.echojournalcmp.core.presentation.designsystem.theme.Excited35
import org.example.echojournalcmp.core.presentation.designsystem.theme.Excited80
import org.example.echojournalcmp.core.presentation.designsystem.theme.Neutral25
import org.example.echojournalcmp.core.presentation.designsystem.theme.Neutral35
import org.example.echojournalcmp.core.presentation.designsystem.theme.Neutral80
import org.example.echojournalcmp.core.presentation.designsystem.theme.Peaceful25
import org.example.echojournalcmp.core.presentation.designsystem.theme.Peaceful35
import org.example.echojournalcmp.core.presentation.designsystem.theme.Peaceful80
import org.example.echojournalcmp.core.presentation.designsystem.theme.Sad25
import org.example.echojournalcmp.core.presentation.designsystem.theme.Sad35
import org.example.echojournalcmp.core.presentation.designsystem.theme.Sad80
import org.example.echojournalcmp.core.presentation.designsystem.theme.Stressed25
import org.example.echojournalcmp.core.presentation.designsystem.theme.Stressed35
import org.example.echojournalcmp.core.presentation.designsystem.theme.Stressed80
import org.example.echojournalcmp.core.presentation.util.UiText
import org.jetbrains.compose.resources.DrawableResource

enum class MoodUi(
    val iconSet: MoodIconSet,
    val colorSet: MoodColorSet,
    val title: UiText
) {
    STRESSED(
        iconSet = MoodIconSet(
            fill = Res.drawable.emoji_stressed,
            outline = Res.drawable.emoji_stressed_outline
        ),
        colorSet = MoodColorSet(
            vivid = Stressed80,
            desaturated = Stressed35,
            faded = Stressed25
        ),
        title = UiText.LocalizedString(Res.string.stressed)
    ),
    SAD(
        iconSet = MoodIconSet(
            fill = Res.drawable.emoji_sad,
            outline = Res.drawable.emoji_stressed_outline
        ),
        colorSet = MoodColorSet(
            vivid = Sad80,
            desaturated = Sad35,
            faded = Sad25
        ),
        title = UiText.LocalizedString(Res.string.sad)
    ),
    NEUTRAL(
        iconSet = MoodIconSet(
            fill = Res.drawable.emoji_neutral,
            outline = Res.drawable.emoji_neutral_outline
        ),
        colorSet = MoodColorSet(
            vivid = Neutral80,
            desaturated = Neutral35,
            faded = Neutral25
        ),
        title = UiText.LocalizedString(Res.string.neutral)

    ),
    PEACEFUL(
        iconSet = MoodIconSet(
            fill = Res.drawable.emoji_peaceful,
            outline = Res.drawable.emoji_peaceful_outline
        ),
        colorSet = MoodColorSet(
            vivid = Peaceful80,
            desaturated = Peaceful35,
            faded = Peaceful25
        ),
        title = UiText.LocalizedString(Res.string.peaceful)

    ),
    EXCITED(
        iconSet = MoodIconSet(
            fill = Res.drawable.emoji_excited,
            outline = Res.drawable.emoji_excited_outline
        ),
        colorSet = MoodColorSet(
            vivid = Excited80,
            desaturated = Excited35,
            faded = Excited25
        ),
        title = UiText.LocalizedString(Res.string.excited)
    )
}

data class MoodIconSet(
    val fill: DrawableResource,
    val outline: DrawableResource
)

data class MoodColorSet(
    val vivid: Color,
    val desaturated: Color,
    val faded: Color
)
