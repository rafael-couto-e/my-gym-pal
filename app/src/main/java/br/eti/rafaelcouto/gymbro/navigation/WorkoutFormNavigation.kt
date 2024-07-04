package br.eti.rafaelcouto.gymbro.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.eti.rafaelcouto.gymbro.presentation.screens.WorkoutFormScreen
import br.eti.rafaelcouto.gymbro.presentation.uistate.MainActivityUiState
import br.eti.rafaelcouto.gymbro.presentation.viewmodel.WorkoutFormViewModel

const val workoutFormRoute = "workout"
const val workoutIdArg = "workoutId"
private const val workoutFormFullRoute = "$workoutFormRoute?$workoutIdArg={$workoutIdArg}"

fun NavGraphBuilder.workoutFormScreen(
    navController: NavHostController,
    setMainActivityState: (MainActivityUiState) -> Unit = {},
    showMessage: (String) -> Unit = {}
) {
    composable(
        route = workoutFormFullRoute,
        arguments = listOf(
            navArgument(workoutIdArg) {
                type = NavType.LongType
                defaultValue = 0L
            }
        )
    ) {
        val viewModel: WorkoutFormViewModel = hiltViewModel()
        val state by viewModel.uiState.collectAsState()

        LaunchedEffect(Unit) { viewModel.loadContent() }

        WorkoutFormScreen(
            onSaveWorkout = {
                viewModel.saveWorkout()
                navController.popBackStack()
            },
            showMessage = showMessage,
            setMainActivityState = setMainActivityState,
            state = state
        )
    }
}

fun NavController.navigateToWorkoutForm() {
    navigate(workoutFormRoute)
}

fun NavController.navigateToWorkoutForm(id: Long) {
    navigate("$workoutFormRoute?$workoutIdArg=$id")
}
