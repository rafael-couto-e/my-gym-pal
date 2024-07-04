package br.eti.rafaelcouto.gymbro.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.eti.rafaelcouto.gymbro.domain.model.Workout
import br.eti.rafaelcouto.gymbro.presentation.screens.WorkoutListScreen
import br.eti.rafaelcouto.gymbro.presentation.uistate.MainActivityUiState
import br.eti.rafaelcouto.gymbro.presentation.viewmodel.WorkoutListViewModel

const val workoutListRoute = "workoutList"

fun NavGraphBuilder.workoutListScreen(
    navController: NavHostController,
    setMainActivityState: (MainActivityUiState) -> Unit = {}
) {
    composable(route = workoutListRoute) {
        val viewModel: WorkoutListViewModel = hiltViewModel()
        val state by viewModel.uiState.collectAsState()

        LaunchedEffect(Unit) { viewModel.loadContent() }

        WorkoutListScreen(
            onWorkoutSelected = { workout ->
                navController.navigateToExerciseList(workout)
            },
            onFabClicked = {
                navController.navigateToWorkoutForm()
            },
            setMainActivityState = setMainActivityState,
            state = state
        )
    }
}

private fun NavController.navigateToExerciseList(workout: Workout) {
    navigate(route = "$workoutFormRoute/${workout.id}/$exerciseListRoute")
}
