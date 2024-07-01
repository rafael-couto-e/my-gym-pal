package br.eti.rafaelcouto.mygympal.domain.mapper

import br.eti.rafaelcouto.mygympal.data.structure.CircularLinkedList
import br.eti.rafaelcouto.mygympal.domain.model.Workout

interface WorkoutMapperAbs {

    fun map(input: List<Workout>): CircularLinkedList<Workout>
}
