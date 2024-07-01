package br.eti.rafaelcouto.mygympal.presentation.screens

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import br.eti.rafaelcouto.mygympal.R
import br.eti.rafaelcouto.mygympal.presentation.components.BottomButtonColumn
import br.eti.rafaelcouto.mygympal.presentation.components.TextField
import br.eti.rafaelcouto.mygympal.presentation.uistate.ExerciseFormUiState
import br.eti.rafaelcouto.mygympal.presentation.uistate.MainActivityUiState

@Composable
fun ExerciseFormScreen(
    onSaveExercise: () -> Unit = {},
    onDeleteExerciseClicked: () -> Unit = {},
    setMainActivityState: (MainActivityUiState) -> Unit = {},
    state: ExerciseFormUiState = ExerciseFormUiState()
) {
    val context = LocalContext.current
    val deleteExerciseMessage = stringResource(id = R.string.exercise_deleted)
    val saveExerciseMessage = stringResource(state.successMessage)

    BottomButtonColumn(
        buttonText = stringResource(id = R.string.save_exercise),
        isButtonEnabled = state.isButtonEnabled,
        onButtonClick = {
            onSaveExercise()
            Toast.makeText(context, saveExerciseMessage, Toast.LENGTH_LONG).show()
            // TODO replace toast with snackbar
        },
        content = {
            TextField(
                value = state.exerciseName,
                label = stringResource(id = R.string.exercise_name),
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
                onValueChange = state.onExerciseNameChange
            )
            TextField(
                value = state.numberOfSets,
                label = stringResource(id = R.string.number_of_sets),
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                onValueChange = state.onNumberOfSetsChange
            )
            TextField(
                value = state.minReps,
                label = stringResource(id = R.string.min_reps),
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                onValueChange = state.onMinRepsChange
            )
            TextField(
                value = state.maxReps,
                label = stringResource(id = R.string.max_reps),
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                onValueChange = state.onMaxRepsChange
            )
            TextField(
                value = state.load,
                label = stringResource(id = R.string.load),
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
                onValueChange = state.onLoadChange
            )
        }
    )

    LaunchedEffect(Unit) {
        val titleRes = if (state.exerciseId == 0L)
            R.string.add_exercise
        else
            R.string.edit_exercise

        val mainState = MainActivityUiState(
            titleRes = titleRes,
            showsBackButton = true,
            topAppBarActions = {
                if (state.exerciseId != 0L)
                    IconButton(
                        onClick = {
                            onDeleteExerciseClicked()
                            Toast.makeText(context, deleteExerciseMessage, Toast.LENGTH_LONG).show()
                            // TODO replace toast with snackbar
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = stringResource(id = R.string.delete_exercise)
                            )
                        }
                    )
            }
        )
        setMainActivityState(mainState)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ExerciseFormScreenDefaultPreview() {
    ExerciseFormScreen()
}

@Preview(showSystemUi = true)
@Composable
private fun ExerciseFormScreenFilledPreview() {
    ExerciseFormScreen(
        state = ExerciseFormUiState(
            exerciseName = "Exercise",
            numberOfSets = "3",
            minReps = "8",
            maxReps = "12",
            load = "50",
            isButtonEnabled = true
        )
    )
}
