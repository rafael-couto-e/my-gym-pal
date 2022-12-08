package br.eti.rafaelcouto.mygympal.domain.usecase

import br.eti.rafaelcouto.mygympal.data.structure.CircularLinkedList
import br.eti.rafaelcouto.mygympal.domain.model.Grupo
import kotlinx.coroutines.flow.Flow

interface GrupoUseCaseAbs {

    fun listaGrupos(): Flow<CircularLinkedList<Grupo>>
    fun localizaGrupo(id: Long): Grupo
    fun salvaGrupo(grupo: Grupo)
    fun alteraGrupo(grupo: Grupo)
    fun excluiGrupo(grupo: Grupo)
}
