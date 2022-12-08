package br.eti.rafaelcouto.mygympal.data.repository

import br.eti.rafaelcouto.mygympal.domain.model.Exercicio
import kotlinx.coroutines.flow.Flow

interface ExercicioRepositoryAbs {

    fun listaExercicios(idGrupo: Long): Flow<List<Exercicio>>
    fun localizaExercicio(id: Long): Exercicio
    fun salvaExercicio(exercicio: Exercicio)
    fun alteraExercicio(exercicio: Exercicio)
    fun excluiExercicio(exercicio: Exercicio)
}
