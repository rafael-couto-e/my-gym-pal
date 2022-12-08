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
    var nomeGrupo by mutableStateOf("")

    fun carregaGrupo(): Flow<Grupo> = flow {
        if (idGrupo != 0L) emit(grupoUseCase.localizaGrupo(idGrupo))
    }

    fun excluiGrupo() {
        if (idGrupo != 0L) {
            val grupo = grupoUseCase.localizaGrupo(idGrupo)
            grupoUseCase.excluiGrupo(grupo)
        }
    }

    fun atualizaGrupo() {
        if (idGrupo != 0L && nomeGrupo.trim().isNotEmpty()) {
            val grupo = grupoUseCase.localizaGrupo(idGrupo)
            val novoGrupo = Grupo(id = grupo.id, nome = nomeGrupo)
            grupoUseCase.alteraGrupo(novoGrupo)
        }
    }

    fun listaExercicios(idGrupo: Long) = exercicioUseCase.listaExercicios(idGrupo)

    fun excluiExercicio(id: Long) {
        val exercicio = exercicioUseCase.localizaExercicio(id)
        exercicioUseCase.excluiExercicio(exercicio)
    }

    fun aumentaCarga(exercicio: Exercicio) {
        exercicioUseCase.aumentaCarga(exercicio)
    }

    fun reduzCarga(exercicio: Exercicio) = exercicioUseCase.reduzCarga(exercicio)
    fun concluiExercicio(exercicio: Exercicio.UI) = exercicioUseCase.concluiExercicio(exercicio)

    suspend fun concluiGrupo(exercicios: List<Exercicio.UI>) {
        val id = exercicios.firstOrNull()?.original?.idGrupo ?: return

        grupoUseCase.listaGrupos().collect { grupos ->
            val grupo = grupos.filter { it.value.id == id }.firstOrNull() ?: return@collect
            val ultimo = grupos.filter { it.value.ultimo }.firstOrNull()

            grupo.value.ultimo = false
            grupo.next?.value?.ultimo = true
            ultimo?.value?.ultimo = false

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
}
