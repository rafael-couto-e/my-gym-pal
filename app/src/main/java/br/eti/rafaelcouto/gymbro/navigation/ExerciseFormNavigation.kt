package br.eti.rafaelcouto.gymbro.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.eti.rafaelcouto.gymbro.presentation.screens.ExerciseFormScreen
import br.eti.rafaelcouto.gymbro.presentation.uistate.MainActivityUiState
import br.eti.rafaelcouto.gymbro.presentation.viewmodel.ExerciseFormViewModel

const val exerciseFormRoute = "exercise"
const val exerciseIdArg = "exerciseId"
private const val exerciseFormFullRoute = "$workoutFormRoute/{$workoutIdArg}/$exerciseFormRoute?$exerciseIdArg={$exerciseIdArg}"

fun NavGraphBuilder.exerciseFormScreen(
    navController: NavController,
    setMainActivityState: (MainActivityUiState) -> Unit = {},
    showMessage: (String) -> Unit = {}
) {
    composable(
        route = exerciseFormFullRoute,
        arguments = listOf(
            navArgument(workoutIdArg) {
                type = NavType.LongType
                defaultValue = 0L
            },
            navArgument(exerciseIdArg) {
                type = NavType.LongType
                defaultValue = 0L
            }
        )
    ) {
        val viewModel: ExerciseFormViewModel = hiltViewModel()
        val state by viewModel.uiState.collectAsState()

        ExerciseFormScreen(
            onSaveExercise = {
                viewModel.saveExercise()
                navController.popBackStack()
            },
            onDeleteExerciseClicked = {
                viewModel.deleteExercise()
                navController.popBackStack()
            },
            showMessage = showMessage,
            setMainActivityState = setMainActivityState,
            state = state
        )
    }
}
