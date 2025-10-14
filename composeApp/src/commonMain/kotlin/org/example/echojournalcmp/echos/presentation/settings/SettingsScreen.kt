@file:OptIn(ExperimentalMaterial3Api::class)

package org.example.echojournalcmp.echos.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import echojournalcmp.composeapp.generated.resources.Res
import echojournalcmp.composeapp.generated.resources.back
import echojournalcmp.composeapp.generated.resources.settings
import org.example.echojournalcmp.core.presentation.designsystem.theme.EchoJournalCMPTheme
import org.example.echojournalcmp.core.presentation.designsystem.theme.bgGradient
import org.example.echojournalcmp.core.presentation.util.defaultShadow
import org.example.echojournalcmp.echos.presentation.settings.components.DefaultTopicSelectorCard
import org.example.echojournalcmp.echos.presentation.settings.components.MoodCard
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SettingsScreen(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.settings),
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = Center
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(SettingsAction.OnBackClick)
                        }
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.back),
                            contentDescription = "Go Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .background(
                        brush = MaterialTheme.colorScheme.bgGradient
                    )
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = spacedBy(16.dp)
            ) {
                MoodCard(
                    modifier = Modifier
                        .defaultShadow(RoundedCornerShape(8.dp)),
                    selectedMood = state.selectedMood,
                    onMoodClick = {
                        onAction(SettingsAction.OnMoodClick(it))
                    }
                )

                DefaultTopicSelectorCard(
                    modifier = Modifier
                        .defaultShadow(RoundedCornerShape(8.dp)),
                    topics = state.topics,
                    searchText = state.searchText,
                    topicSuggestions = state.suggestedTopics,
                    showCreateTopicOption = state.showCreateTopicOption,
                    onSearchTextChange = {
                        onAction(SettingsAction.OnSearchTextChange(it))
                    },
                    canInputText = state.isTopicTextInputVisible,
                    showSuggestionsDropDown = state.isTopicSuggestionsVisible,
                    onTableCanInputText = {
                        onAction(SettingsAction.OnAddButtonClick)
                    },
                    onDismissSuggestionsDropDown = {
                        onAction(SettingsAction.OnDismissTopicDropDown)
                    },
                    onRemoveTopicClick = {
                        onAction(SettingsAction.OnRemoveTopicClick(it))
                    },
                    onAddTopicClick = {
                        onAction(SettingsAction.OnSelectTopicClick(it))
                    }
                )
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    EchoJournalCMPTheme {
        SettingsScreen(
            state = SettingsState(),
            onAction = {}
        )
    }
}