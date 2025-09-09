package org.example.echojournalcmp.echos.presentation.echos.model

import org.example.echojournalcmp.core.presentation.util.UiText
import org.example.echojournalcmp.echos.presentation.model.EchoUi

data class EchoDaySection(
    val dataHeader: UiText,
    val echos: List<EchoUi>
)
