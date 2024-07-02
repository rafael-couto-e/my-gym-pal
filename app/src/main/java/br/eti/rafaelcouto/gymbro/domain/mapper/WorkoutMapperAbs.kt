package br.eti.rafaelcouto.gymbro.domain.mapper

import br.eti.rafaelcouto.gymbro.data.structure.CircularLinkedList
import br.eti.rafaelcouto.gymbro.domain.model.Workout

interface WorkoutMapperAbs {

    fun map(input: List<Workout>): CircularLinkedList<Workout>
}
