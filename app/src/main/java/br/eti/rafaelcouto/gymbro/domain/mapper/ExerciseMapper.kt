package br.eti.rafaelcouto.gymbro.domain.mapper

import br.eti.rafaelcouto.gymbro.domain.model.Exercise
import javax.inject.Inject

class ExerciseMapper @Inject constructor() : ExerciseMapperAbs {

    override fun map(input: List<Exercise>) = input.map { Exercise.UI(it) }
}
