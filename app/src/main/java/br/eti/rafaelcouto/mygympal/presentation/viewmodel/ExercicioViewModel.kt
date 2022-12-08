package br.eti.rafaelcouto.mygympal.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.eti.rafaelcouto.mygympal.domain.model.Exercicio
import br.eti.rafaelcouto.mygympal.domain.usecase.ExercicioUseCaseAbs
import br.eti.rafaelcouto.mygympal.presentation.toIntOrZero
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
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

    fun salvaExercicio(idGrupo: Long) {
        val exercicio = Exercicio(
            nome = nomeExercicio,
            numSeries = numSeries.toByte(),
            minRepeticoes = minRepeticoes.toByte(),
            maxRepeticoes = maxRepeticoes.toByte(),
            carga = carga.toShort(),
            idGrupo = idGrupo
        )

        useCase.salvaExercicio(exercicio)

        nomeExercicio = ""
        numSeries = ""
        minRepeticoes = ""
        maxRepeticoes = ""
        carga = ""
        podeContinuar = false
    }

    fun validaBotao() {
        podeContinuar = nomeExercicio.isNotBlank() &&
                numSeries.toIntOrZero() > 0 &&
                minRepeticoes.toIntOrZero() > 0 &&
                maxRepeticoes.toIntOrZero() > 0 &&
                maxRepeticoes.toIntOrZero() >= minRepeticoes.toIntOrZero() &&
                carga.toIntOrZero() > 0
    }
}
