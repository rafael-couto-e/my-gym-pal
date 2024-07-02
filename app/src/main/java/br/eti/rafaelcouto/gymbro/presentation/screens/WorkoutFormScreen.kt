package br.eti.rafaelcouto.gymbro.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import br.eti.rafaelcouto.gymbro.R
import br.eti.rafaelcouto.gymbro.presentation.components.BottomButtonColumn
import br.eti.rafaelcouto.gymbro.presentation.components.TextField
import br.eti.rafaelcouto.gymbro.presentation.uistate.MainActivityUiState
import br.eti.rafaelcouto.gymbro.presentation.uistate.WorkoutFormScreenUiState

@Composable
fun WorkoutFormScreen(
    onSaveWorkout: () -> Unit = {},
    setMainActivityState: (MainActivityUiState) -> Unit = {},
    showMessage: (String) -> Unit = {},
    state: WorkoutFormScreenUiState = WorkoutFormScreenUiState()
) {
    val successMessage = stringResource(id = state.successMessage)

    BottomButtonColumn(
        buttonText = stringResource(id = R.string.save_workout),
        isButtonEnabled = state.isButtonEnabled,
        onButtonClick = {
            onSaveWorkout()
            showMessage(successMessage)
        },
        content = {
            TextField(
                value = state.workoutName,
                label = stringResource(id = R.string.workout_name),
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done,
                onValueChange = {
                    state.onWorkoutNameChanged(it)
                }
            )
        }
    )

    LaunchedEffect(state.workoutId) {
        val titleRes = if (state.workoutId == 0L)
            R.string.create_workout
        else
            R.string.edit_workout

        val mainState = MainActivityUiState(
            titleRes = titleRes,
            showsBackButton = true
        )
        setMainActivityState(mainState)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun WorkoutFormScreenDefaultPreview() {
    WorkoutFormScreen()
}

@Preview(showSystemUi = true)
@Composable
private fun WorkoutFormScreenFilledPreview() {
    WorkoutFormScreen(
        state = WorkoutFormScreenUiState(
            workoutName = "Workout",
            isButtonEnabled = true
        )
    )
}
