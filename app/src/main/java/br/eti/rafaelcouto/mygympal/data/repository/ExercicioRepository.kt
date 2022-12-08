package br.eti.rafaelcouto.mygympal.data.repository

import br.eti.rafaelcouto.mygympal.data.source.ExercicioDao
import br.eti.rafaelcouto.mygympal.domain.model.Exercicio
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExercicioRepository @Inject constructor(
    private val dao: ExercicioDao
) : ExercicioRepositoryAbs {

    override fun listaExercicios(idGrupo: Long) = flow {
        emit(dao.listaExercicios(idGrupo))
    }

    override fun localizaExercicio(id: Long) = dao.localizaExercicio(id)
    override fun salvaExercicio(exercicio: Exercicio) = dao.salvaExercicio(exercicio)
    override fun alteraExercicio(exercicio: Exercicio) = dao.alteraExercicio(exercicio)
    override fun excluiExercicio(exercicio: Exercicio) = dao.excluiExercicio(exercicio)
}
