package br.eti.rafaelcouto.mygympal.navigation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.eti.rafaelcouto.mygympal.presentation.uistate.MainActivityUiState

@Composable
fun MyGymPalNavHost(
    setMainActivityState: (MainActivityUiState) -> Unit = {},
    showMessage: (message: String) -> Unit = {},
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = workoutListRoute,
        builder = {
            workoutListScreen(
                navController = navController,
                setMainActivityState = setMainActivityState
            )
            workoutFormScreen(
                navController = navController,
                setMainActivityState = setMainActivityState,
                showMessage = showMessage
            )
            exerciseListScreen(
                navController = navController,
                setMainActivityState = setMainActivityState,
                showMessage = showMessage
            )
            exerciseFormScreen(
                navController = navController,
                setMainActivityState = setMainActivityState,
                showMessage = showMessage
            )
        }
    )
}
