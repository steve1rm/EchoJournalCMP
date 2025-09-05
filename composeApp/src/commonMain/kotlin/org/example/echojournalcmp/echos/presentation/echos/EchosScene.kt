package org.example.echojournalcmp.echos.presentation.echos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun EchosScene(
    viewModel: EchosViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    EchosScreen(
        state = state,
        onAction = viewModel::onAction
    )
}