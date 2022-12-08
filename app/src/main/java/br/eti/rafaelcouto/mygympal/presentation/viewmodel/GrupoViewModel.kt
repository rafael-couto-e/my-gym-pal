package br.eti.rafaelcouto.mygympal.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.eti.rafaelcouto.mygympal.domain.model.Grupo
import br.eti.rafaelcouto.mygympal.domain.usecase.GrupoUseCaseAbs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class GrupoViewModel @Inject constructor(
    private val useCase: GrupoUseCaseAbs
) : ViewModel() {

    var nomeGrupo by mutableStateOf("")
    var habilitaBotao by mutableStateOf(false)

    suspend fun salvaGrupo() {
        useCase.listaGrupos().map { grupos ->
            grupos.filter { it.value.ultimo }.firstOrNull()
        }.collect { ultimo ->
            val grupo = Grupo(nome = nomeGrupo.trim(), ultimo = ultimo == null)
            useCase.salvaGrupo(grupo)
            nomeGrupo = ""
            habilitaBotao = false
        }
    }
}
