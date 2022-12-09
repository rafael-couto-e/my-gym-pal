package br.eti.rafaelcouto.mygympal.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.eti.rafaelcouto.mygympal.domain.model.Exercicio
import br.eti.rafaelcouto.mygympal.domain.usecase.ExercicioUseCaseAbs
import br.eti.rafaelcouto.mygympal.presentation.toIntOrZero
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExercicioViewModel @Inject constructor(
    private val useCase: ExercicioUseCaseAbs
) : ViewModel() {

    var nomeExercicio by mutableStateOf("")
    var numSeries by mutableStateOf("")
    var minRepeticoes by mutableStateOf("")
    var maxRepeticoes by mutableStateOf("")
    var carga by mutableStateOf("")
    var podeContinuar by mutableStateOf(false)
    var idExercicio by mutableStateOf(0L)

    fun carregaExercicio(idExercicio: Long) {
        this.idExercicio = idExercicio
        val exercicio = useCase.localizaExercicio(idExercicio)

        nomeExercicio = exercicio.nome
        numSeries = exercicio.numSeries.toString()
        minRepeticoes = exercicio.minRepeticoes.toString()
        maxRepeticoes = exercicio.maxRepeticoes.toString()
        carga = exercicio.carga.toString()
        podeContinuar = true
    }

    fun salvaExercicio(idGrupo: Long) {
        val exercicio = if (idExercicio == 0L) Exercicio(
            nome = nomeExercicio,
            numSeries = numSeries.toByte(),
            minRepeticoes = minRepeticoes.toByte(),
            maxRepeticoes = maxRepeticoes.toByte(),
            carga = carga.toShort(),
            idGrupo = idGrupo
        ) else Exercicio(
            id = idExercicio,
            nome = nomeExercicio,
            numSeries = numSeries.toByte(),
            minRepeticoes = minRepeticoes.toByte(),
            maxRepeticoes = maxRepeticoes.toByte(),
            carga = carga.toShort(),
            idGrupo = idGrupo
        )

        if (idExercicio == 0L)
            useCase.salvaExercicio(exercicio)
        else
            useCase.atualizaExercicio(exercicio)
    }

    fun excluiExercicio(id: Long) {
        val exercicio = useCase.localizaExercicio(id)
        useCase.excluiExercicio(exercicio)
    }

    fun validaBotao() {
        podeContinuar = nomeExercicio.isNotBlank() &&
                numSeries.toIntOrZero() > 0 &&
                minRepeticoes.toIntOrZero() > 0 &&
                maxRepeticoes.toIntOrZero() > 0 &&
                maxRepeticoes.toIntOrZero() >= minRepeticoes.toIntOrZero() &&
                carga.toIntOrZero() > 0
    }

    fun resetaDados() {
        nomeExercicio = ""
        numSeries = ""
        minRepeticoes = ""
        maxRepeticoes = ""
        carga = ""
        podeContinuar = false
        idExercicio = 0L
    }
}
