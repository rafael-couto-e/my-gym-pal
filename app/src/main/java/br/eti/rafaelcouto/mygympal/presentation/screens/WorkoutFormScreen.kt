package br.eti.rafaelcouto.mygympal.presentation.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import br.eti.rafaelcouto.mygympal.R
import br.eti.rafaelcouto.mygympal.presentation.components.BottomButtonColumn
import br.eti.rafaelcouto.mygympal.presentation.components.TextField
import br.eti.rafaelcouto.mygympal.presentation.uistate.MainActivityUiState
import br.eti.rafaelcouto.mygympal.presentation.uistate.WorkoutFormScreenUiState

@Composable
fun WorkoutFormScreen(
    onSaveWorkout: () -> Unit = {},
    setMainActivityState: (MainActivityUiState) -> Unit = {},
    state: WorkoutFormScreenUiState = WorkoutFormScreenUiState()
) {
    val context = LocalContext.current
    val successMessage = stringResource(id = state.successMessage)

    BottomButtonColumn(
        buttonText = stringResource(id = R.string.save_workout),
        isButtonEnabled = state.isButtonEnabled,
        onButtonClick = {
            onSaveWorkout()
            Toast.makeText(context, successMessage, Toast.LENGTH_LONG).show()
            // TODO replace toast with snackbar
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

    LaunchedEffect(Unit) {
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
