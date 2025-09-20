package org.example.echojournalcmp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.example.echojournalcmp.create_echo.CreateEchoScene
import org.example.echojournalcmp.echos.presentation.echos.EchosScene
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
            CreateEchoScene {
                navigationController.navigateUp()
            }
        }
    }
}