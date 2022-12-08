package br.eti.rafaelcouto.mygympal.data.repository

import br.eti.rafaelcouto.mygympal.domain.model.Grupo
import kotlinx.coroutines.flow.Flow

interface GrupoRepositoryAbs {
    fun listaGrupos(): Flow<List<Grupo>>
    fun localizaGrupo(id: Long): Grupo
    fun salvaGrupo(grupo: Grupo)
    fun alteraGrupo(grupo: Grupo)
    fun excluiGrupo(grupo: Grupo)
}
