package br.eti.rafaelcouto.gymbro.presentation.uistate

import androidx.annotation.StringRes
import br.eti.rafaelcouto.gymbro.R

data class ExerciseFormUiState(
    val exerciseId: Long = 0,
    val exerciseName: String = "",
    val onExerciseNameChange: (String) -> Unit = {},
    val numberOfSets: String = "",
    val onNumberOfSetsChange: (String) -> Unit = {},
    val minReps: String = "",
    val onMinRepsChange: (String) -> Unit = {},
    val maxReps: String = "",
    val onMaxRepsChange: (String) -> Unit = {},
    val load: String = "",
    val onLoadChange: (String) -> Unit = {},
    val workoutId: Long = 0L,
    val isButtonEnabled: Boolean = false,
    @StringRes val successMessage: Int = R.string.exercise_created
)
