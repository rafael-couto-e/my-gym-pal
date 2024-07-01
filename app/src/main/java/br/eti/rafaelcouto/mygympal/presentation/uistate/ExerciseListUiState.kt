package br.eti.rafaelcouto.mygympal.presentation.uistate

import br.eti.rafaelcouto.mygympal.domain.model.Exercise
import br.eti.rafaelcouto.mygympal.domain.model.Workout

data class ExerciseListUiState(
    val isMenuExpanded: Boolean = false,
    val workout: Workout = Workout(),
    val exercises: List<Exercise.UI> = emptyList(),
    val canFinishWorkout: Boolean = true
) {
    val shouldDisplayEmptyMessage
        get() = exercises.isEmpty()
}
