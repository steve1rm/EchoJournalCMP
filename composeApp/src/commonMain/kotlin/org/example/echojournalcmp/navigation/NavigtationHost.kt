package org.example.echojournalcmp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.example.echojournalcmp.echos.presentation.echos.EchosScene
import org.example.echojournalcmp.echos.presentation.echos.EchosScreen
import org.example.echojournalcmp.toCreateEchoRoute


@Composable
fun NavigationHost(
    navigationController: NavHostController
) {
    NavHost(
        navController = navigationController,
        startDestination = NavigationRoute.Echos
    ) {
        composable<NavigationRoute.Echos> {
            EchosScene(
                onNavigateToCreateEcho = { recordingDetails ->
                    navigationController.navigate(
                        recordingDetails.toCreateEchoRoute())
                }
            )
        }

        composable<NavigationRoute.CreateEchos> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Create Echo Screen")
            }
        }
    }
}