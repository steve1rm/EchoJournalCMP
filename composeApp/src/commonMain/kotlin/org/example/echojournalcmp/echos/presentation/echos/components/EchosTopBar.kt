@file:OptIn(ExperimentalMaterial3Api::class)

package org.example.echojournalcmp.echos.presentation.echos.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import echojournalcmp.composeapp.generated.resources.Res
import echojournalcmp.composeapp.generated.resources.app_name
import echojournalcmp.composeapp.generated.resources.settings
import echojournalcmp.composeapp.generated.resources.title_echos
import org.example.echojournalcmp.core.presentation.designsystem.theme.EchoJournalCMPTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun EchosTopBar(
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(Res.string.title_echos),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        actions = {
            IconButton(
                onClick = onSettingsClick
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.settings),
                    contentDescription = stringResource(Res.string.settings),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Preview
@Composable
fun EchosTopBarPreview() {
    EchoJournalCMPTheme {
        EchosTopBar(onSettingsClick = {})
    }
}
