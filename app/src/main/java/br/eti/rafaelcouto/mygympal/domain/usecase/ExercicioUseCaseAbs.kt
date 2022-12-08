package br.eti.rafaelcouto.mygympal.domain.usecase

import br.eti.rafaelcouto.mygympal.domain.model.Exercicio
import kotlinx.coroutines.flow.Flow

interface ExercicioUseCaseAbs {

    fun listaExercicios(idGrupo: Long): Flow<List<Exercicio.UI>>
    fun localizaExercicio(id: Long): Exercicio
    fun salvaExercicio(exercicio: Exercicio)
    fun excluiExercicio(exercicio: Exercicio)
    fun concluiExercicio(exercicio: Exercicio.UI)
    fun concluiGrupo(exercicios: List<Exercicio.UI>)
    fun aumentaCarga(exercicio: Exercicio)
    fun reduzCarga(exercicio: Exercicio)
}
