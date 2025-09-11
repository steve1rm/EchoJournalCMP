package org.example.echojournalcmp.echos.presentation.echos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EchosScene() {
    val viewModel = koinViewModel<EchosViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    EchosScreen(
        state = state,
        onAction = viewModel::onAction
    )
}