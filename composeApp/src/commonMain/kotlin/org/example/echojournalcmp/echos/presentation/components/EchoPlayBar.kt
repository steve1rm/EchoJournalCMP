package org.example.echojournalcmp.echos.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.echojournalcmp.core.presentation.designsystem.theme.EchoJournalCMPTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.random.Random

@Composable
fun EchoPlayBar(
    amplitudeBarWidth: Dp,
    amplitudeBarSpacing: Dp,
    powerRatios: List<Float>,
    trackColor: Color,
    trackFillColor: Color,
    playerProgress: () -> Float,
    modifier: Modifier = Modifier) {

    Canvas(
        modifier = modifier
    ) {
        val amplitudeBarWidthPx = amplitudeBarWidth.toPx()
        val amplitudeBarSpacingPx = amplitudeBarSpacing.toPx()

        val clipPath = Path()

        powerRatios.forEachIndexed { index, ratio ->
            val height = ratio * size.height

            val xOffSet = index * (amplitudeBarSpacingPx + amplitudeBarWidthPx)
            val yTopStart = this.center.y - height / 2f

            val topLeft = Offset(
                x = xOffSet,
                y = yTopStart
            )
            val rectSize = Size(
                width = amplitudeBarWidthPx,
                height = height
            )
            val roundRect = RoundRect(
                rect = Rect(
                    offset = topLeft,
                    size = rectSize,
                ),
                cornerRadius = CornerRadius(100f))

            clipPath.addRoundRect(roundRect)

            this.drawRoundRect(
                color = trackColor,
                topLeft = topLeft,
                size = rectSize,
                cornerRadius = CornerRadius(100f)
            )

            clipPath(clipPath) {
                this.drawRect(
                    color = trackFillColor,
                    size = Size(
                        width = this.size.width * playerProgress(),
                        height = this.size.height
                    )
                )
            }

            this.drawRoundRect(
                color = trackColor,
                topLeft = topLeft,
                size = rectSize,
                cornerRadius = CornerRadius(100f)
            )
        }
    }
}

@Preview
@Composable
fun EchoPlayBarPreview() {
    val powerRatios = remember {
        (1..30).map {
            Random.nextFloat()
        }
    }
    val playerProgress = { 0.095f }

    EchoJournalCMPTheme {
        EchoPlayBar(
            amplitudeBarWidth = 4.dp,
            amplitudeBarSpacing = 3.dp,
            powerRatios = powerRatios,
            trackColor = Color.LightGray,
            trackFillColor = Color.DarkGray,
            playerProgress = playerProgress,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
    }
}
