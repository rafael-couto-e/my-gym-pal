package br.eti.rafaelcouto.mygympal.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.eti.rafaelcouto.mygympal.domain.model.Exercicio
import br.eti.rafaelcouto.mygympal.domain.model.Grupo
import br.eti.rafaelcouto.mygympal.domain.usecase.ExercicioUseCaseAbs
import br.eti.rafaelcouto.mygympal.domain.usecase.GrupoUseCaseAbs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ExerciciosViewModel @Inject constructor(
    private val grupoUseCase: GrupoUseCaseAbs,
    private val exercicioUseCase: ExercicioUseCaseAbs
) : ViewModel() {

    var idGrupo by mutableStateOf(0L)
    var seriesConcluidas: Map<Long, List<Boolean>> by mutableStateOf(hashMapOf())
    var podeConcluir: Map<Long, Boolean> by mutableStateOf(hashMapOf())

    fun carregaGrupo(): Flow<Grupo> = flow {
        if (idGrupo != 0L) emit(grupoUseCase.localizaGrupo(idGrupo))
    }

    fun resetaSeries() {
        seriesConcluidas = hashMapOf()
        podeConcluir = hashMapOf()
    }

    fun iniciaSeriesConcluidas(exercicio: Exercicio.UI) {
        if (seriesConcluidas[exercicio.original.id] != null) return

        val initial = List(exercicio.original.numSeries.toInt()) { false }

        val novo = HashMap(seriesConcluidas)
        novo[exercicio.original.id] = initial
        seriesConcluidas = novo
    }

    fun concluiSerie(idExercicio: Long, index: Int, concluido: Boolean) {
        val list = mutableListOf<Boolean>()

        seriesConcluidas[idExercicio]?.forEachIndexed { mIndex, _ ->
            list.add(
                if (index == mIndex)
                    concluido
                else
                    seriesConcluidas[idExercicio]?.get(mIndex) ?: false
            )
        }

        val novo = HashMap(seriesConcluidas)
        novo[idExercicio] = list.toList()
        seriesConcluidas = novo
    }

    suspend fun excluiGrupo() {
        if (idGrupo != 0L) {
            val grupo = grupoUseCase.localizaGrupo(idGrupo)

            grupoUseCase.listaGrupos().map { grupos ->
                grupos.filter { it.value.id == grupo.id }.first()
            }.collect {
                if (it.value.ultimo) {
                    val novoUltimo = it.next?.value
                    novoUltimo?.let { nu ->
                        nu.ultimo = true
                        grupoUseCase.alteraGrupo(nu)
                    }
                }

                grupoUseCase.excluiGrupo(grupo)
            }
        }
    }

    fun listaExercicios(idGrupo: Long) = exercicioUseCase.listaExercicios(idGrupo)

    fun aumentaCarga(exercicio: Exercicio) {
        exercicioUseCase.aumentaCarga(exercicio)
    }

    fun reduzCarga(exercicio: Exercicio) = exercicioUseCase.reduzCarga(exercicio)

    fun concluiExercicio(exercicio: Exercicio.UI) {
        val novo = HashMap(podeConcluir)
        novo[exercicio.original.id] = false
        podeConcluir = novo

        exercicioUseCase.concluiExercicio(exercicio)
    }

    suspend fun concluiGrupo(exercicios: List<Exercicio.UI>) {
        val id = exercicios.firstOrNull()?.original?.idGrupo ?: return

        grupoUseCase.listaGrupos().collect { grupos ->
            val grupo = grupos.filter { it.value.id == id }.firstOrNull() ?: return@collect
            val ultimo = grupos.filter { it.value.ultimo }.firstOrNull()

            grupo.value.ultimo = false
            ultimo?.value?.ultimo = false
            grupo.next?.value?.ultimo = true

            grupoUseCase.alteraGrupo(grupo.value)

            grupo.next?.value?.let { prox ->
                grupoUseCase.alteraGrupo(prox)
            }

            ultimo?.value?.let { ult ->
                grupoUseCase.alteraGrupo(ult)
            }

            exercicioUseCase.concluiGrupo(exercicios)
        }
    }

    fun concluiSeries(exericicos: List<Exercicio.UI>) {
        val novoSeries = HashMap(seriesConcluidas)
        val novoChecks = HashMap(podeConcluir)

        exericicos.forEach {
            novoSeries[it.original.id] = List(it.original.numSeries.toInt()) { true }
            novoChecks[it.original.id] = false
        }

        seriesConcluidas = novoSeries
        podeConcluir = novoChecks
    }
}
