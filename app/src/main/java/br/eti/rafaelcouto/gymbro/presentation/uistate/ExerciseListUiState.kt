package br.eti.rafaelcouto.gymbro.presentation.uistate

import br.eti.rafaelcouto.gymbro.domain.model.Exercise
import br.eti.rafaelcouto.gymbro.domain.model.Workout

data class ExerciseListUiState(
    val isMenuExpanded: Boolean = false,
    val workout: Workout = Workout(),
    val exercises: List<Exercise.UI> = emptyList(),
    val canFinishWorkout: Boolean = true
) {
    val shouldDisplayEmptyMessage
        get() = exercises.isEmpty()
}
