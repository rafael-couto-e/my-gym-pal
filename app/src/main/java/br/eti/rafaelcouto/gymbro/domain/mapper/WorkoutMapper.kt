package br.eti.rafaelcouto.gymbro.domain.mapper

import br.eti.rafaelcouto.gymbro.data.structure.CircularLinkedList
import br.eti.rafaelcouto.gymbro.domain.model.Workout
import javax.inject.Inject

class WorkoutMapper @Inject constructor() : WorkoutMapperAbs {

    override fun map(input: List<Workout>): CircularLinkedList<Workout> {
        val linkedList = CircularLinkedList<Workout>()
        linkedList.addAll(input)
        return linkedList
    }
}
