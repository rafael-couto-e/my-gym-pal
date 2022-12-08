package br.eti.rafaelcouto.mygympal.domain.usecase

import br.eti.rafaelcouto.mygympal.data.repository.GrupoRepositoryAbs
import br.eti.rafaelcouto.mygympal.domain.mapper.GrupoMapperAbs
import br.eti.rafaelcouto.mygympal.domain.model.Grupo
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GrupoUseCase @Inject constructor(
    private val repository: GrupoRepositoryAbs,
    private val mapper: GrupoMapperAbs
) : GrupoUseCaseAbs {

    override fun listaGrupos() = repository.listaGrupos().map {
        mapper.map(it)
    }

    override fun localizaGrupo(id: Long) = repository.localizaGrupo(id)
    override fun salvaGrupo(grupo: Grupo) = repository.salvaGrupo(grupo)
    override fun alteraGrupo(grupo: Grupo) = repository.alteraGrupo(grupo)
    override fun excluiGrupo(grupo: Grupo) = repository.excluiGrupo(grupo)
}
