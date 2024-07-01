package br.eti.rafaelcouto.mygympal.domain.mapper

import br.eti.rafaelcouto.mygympal.domain.model.Exercise

interface ExerciseMapperAbs {

    fun map(input: List<Exercise>): List<Exercise.UI>
}
