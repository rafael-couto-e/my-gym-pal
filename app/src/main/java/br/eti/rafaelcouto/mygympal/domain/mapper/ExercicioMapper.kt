package br.eti.rafaelcouto.mygympal.domain.mapper

import br.eti.rafaelcouto.mygympal.domain.model.Exercicio
import javax.inject.Inject

class ExercicioMapper @Inject constructor() : ExercicioMapperAbs {

    override fun map(input: List<Exercicio>) = input.map { Exercicio.UI(it) }
}
