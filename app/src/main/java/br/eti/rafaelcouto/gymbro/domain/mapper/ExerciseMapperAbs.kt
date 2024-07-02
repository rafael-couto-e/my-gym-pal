package br.eti.rafaelcouto.gymbro.domain.mapper

import br.eti.rafaelcouto.gymbro.domain.model.Exercise

interface ExerciseMapperAbs {

    fun map(input: List<Exercise>): List<Exercise.UI>
}
