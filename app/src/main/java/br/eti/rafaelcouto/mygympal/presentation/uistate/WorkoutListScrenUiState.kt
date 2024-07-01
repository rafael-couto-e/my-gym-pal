package br.eti.rafaelcouto.mygympal.presentation.uistate

import br.eti.rafaelcouto.mygympal.data.structure.CircularLinkedList
import br.eti.rafaelcouto.mygympal.domain.model.Workout

data class WorkoutListScrenUiState(
    val workouts: CircularLinkedList<Workout> = CircularLinkedList()
) {
    val shouldDisplayEmptyMessage
        get() = workouts.isEmpty()
}
