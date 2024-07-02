package br.eti.rafaelcouto.gymbro.presentation.uistate

import br.eti.rafaelcouto.gymbro.data.structure.CircularLinkedList
import br.eti.rafaelcouto.gymbro.domain.model.Workout

data class WorkoutListScrenUiState(
    val workouts: CircularLinkedList<Workout> = CircularLinkedList()
) {
    val shouldDisplayEmptyMessage
        get() = workouts.isEmpty()
}
