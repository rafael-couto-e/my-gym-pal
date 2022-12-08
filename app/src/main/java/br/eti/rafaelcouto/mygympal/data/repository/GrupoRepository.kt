package br.eti.rafaelcouto.mygympal.data.repository

import br.eti.rafaelcouto.mygympal.data.source.GrupoDao
import br.eti.rafaelcouto.mygympal.domain.model.Grupo
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GrupoRepository @Inject constructor(
    private val dao: GrupoDao
) : GrupoRepositoryAbs {

    override fun listaGrupos() = flow {
        emit(dao.listaGrupos())
    }

    override fun localizaGrupo(id: Long) = dao.localizaGrupo(id)
    override fun salvaGrupo(grupo: Grupo) = dao.salvaGrupo(grupo)
    override fun alteraGrupo(grupo: Grupo) = dao.alteraGrupo(grupo)
    override fun excluiGrupo(grupo: Grupo) = dao.excluiGrupo(grupo)
}
