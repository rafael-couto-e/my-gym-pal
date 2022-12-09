package br.eti.rafaelcouto.mygympal.domain.usecase

import br.eti.rafaelcouto.mygympal.data.repository.ExercicioRepositoryAbs
import br.eti.rafaelcouto.mygympal.domain.mapper.ExercicioMapperAbs
import br.eti.rafaelcouto.mygympal.domain.model.Exercicio
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExercicioUseCase @Inject constructor(
    private val repository: ExercicioRepositoryAbs,
    private val mapper: ExercicioMapperAbs
) : ExercicioUseCaseAbs {

    override fun listaExercicios(idGrupo: Long) = repository.listaExercicios(idGrupo).map {
        mapper.map(it)
    }

    override fun localizaExercicio(id: Long) = repository.localizaExercicio(id)
    override fun salvaExercicio(exercicio: Exercicio) = repository.salvaExercicio(exercicio)
    override fun atualizaExercicio(exercicio: Exercicio) = repository.alteraExercicio(exercicio)
    override fun excluiExercicio(exercicio: Exercicio) = repository.excluiExercicio(exercicio)

    override fun concluiExercicio(exercicio: Exercicio.UI) {
        exercicio.concluido = true
    }

    override fun concluiGrupo(exercicios: List<Exercicio.UI>) = exercicios.forEach {
        concluiExercicio(it)
    }

    override fun aumentaCarga(exercicio: Exercicio) {
        exercicio.carga++
        repository.alteraExercicio(exercicio)
    }

    override fun reduzCarga(exercicio: Exercicio) {
        exercicio.carga--
        repository.alteraExercicio(exercicio)
    }
}
