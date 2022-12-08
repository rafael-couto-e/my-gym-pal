package br.eti.rafaelcouto.mygympal.domain.mapper

import br.eti.rafaelcouto.mygympal.domain.model.Exercicio

interface ExercicioMapperAbs {

    fun map(input: List<Exercicio>): List<Exercicio.UI>
}
