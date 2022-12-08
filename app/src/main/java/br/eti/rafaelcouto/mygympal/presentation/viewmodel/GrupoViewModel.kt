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
    var idGrupo by mutableStateOf(0L)

    fun carregaGrupo(id: Long) {
        idGrupo = id
        nomeGrupo = useCase.localizaGrupo(id).nome
        habilitaBotao = true
    }

    fun resetaDados() {
        nomeGrupo = ""
        habilitaBotao = false
        idGrupo = 0L
    }

    suspend fun salvaGrupo() {
        if (idGrupo != 0L) {
            val grupo = useCase.localizaGrupo(idGrupo)
            val novoGrupo = Grupo(idGrupo, nomeGrupo, grupo.ultimo)
            useCase.alteraGrupo(novoGrupo)
        } else {
            useCase.listaGrupos().map { grupos ->
                grupos.filter { it.value.ultimo }.firstOrNull()
            }.collect { ultimo ->
                val grupo = Grupo(nome = nomeGrupo.trim(), ultimo = ultimo == null)
                useCase.salvaGrupo(grupo)
            }
        }
    }
}
