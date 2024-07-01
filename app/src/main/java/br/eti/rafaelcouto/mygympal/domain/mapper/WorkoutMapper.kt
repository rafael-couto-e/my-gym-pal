package br.eti.rafaelcouto.mygympal.domain.mapper

import br.eti.rafaelcouto.mygympal.data.structure.CircularLinkedList
import br.eti.rafaelcouto.mygympal.domain.model.Workout
import javax.inject.Inject

class WorkoutMapper @Inject constructor() : WorkoutMapperAbs {

    override fun map(input: List<Workout>): CircularLinkedList<Workout> {
        val linkedList = CircularLinkedList<Workout>()
        linkedList.addAll(input)
        return linkedList
    }
}
