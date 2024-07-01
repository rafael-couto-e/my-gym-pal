package br.eti.rafaelcouto.mygympal.domain.mapper

import br.eti.rafaelcouto.mygympal.domain.model.Exercise
import javax.inject.Inject

class ExerciseMapper @Inject constructor() : ExerciseMapperAbs {

    override fun map(input: List<Exercise>) = input.map { Exercise.UI(it) }
}
