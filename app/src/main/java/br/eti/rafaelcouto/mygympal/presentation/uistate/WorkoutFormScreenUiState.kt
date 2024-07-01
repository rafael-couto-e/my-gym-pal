package br.eti.rafaelcouto.mygympal.presentation.uistate

import androidx.annotation.StringRes
import br.eti.rafaelcouto.mygympal.R

data class WorkoutFormScreenUiState(
    val workoutId: Long = 0L,
    val workoutName: String = "",
    val onWorkoutNameChanged: (String) -> Unit = {},
    val isButtonEnabled: Boolean = false,
    @StringRes val successMessage: Int = R.string.workout_created
)
