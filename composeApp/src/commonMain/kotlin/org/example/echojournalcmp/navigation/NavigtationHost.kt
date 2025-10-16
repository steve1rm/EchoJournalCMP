package org.example.echojournalcmp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import org.example.echojournalcmp.create_echo.CreateEchoScene
import org.example.echojournalcmp.echos.presentation.echos.EchosScene
import org.example.echojournalcmp.echos.presentation.settings.SettingsScene
import org.example.echojournalcmp.toCreateEchoRoute

const val ACTION_CREATE_ECHO = "org.example.CREATE_ECHO"

@Composable
fun NavigationHost(
    navigationController: NavHostController
) {
    NavHost(
        navController = navigationController,
        startDestination = NavigationRoute.Echos(
            startRecording = false
        )
    ) {
        composable<NavigationRoute.Echos>(
            deepLinks = listOf(
                navDeepLink<NavigationRoute.Echos>(
                    basePath = "https://echojournalcmp.com/echos"
                ) {
                    action = ACTION_CREATE_ECHO
                }
            )
        ) {
            EchosScene(
                onNavigateToCreateEcho = { recordingDetails ->
                    navigationController.navigate(
                        recordingDetails.toCreateEchoRoute())
                },
                onNavigateToSettings = {
                    navigationController.navigate(
                        NavigationRoute.Settings
                    )
                }
            )
        }

        composable<NavigationRoute.CreateEchos> {
            CreateEchoScene {
                navigationController.navigateUp()
            }
        }

        composable<NavigationRoute.Settings> {
            SettingsScene(
                onBackClick = navigationController::navigateUp
            )
        }
    }
}