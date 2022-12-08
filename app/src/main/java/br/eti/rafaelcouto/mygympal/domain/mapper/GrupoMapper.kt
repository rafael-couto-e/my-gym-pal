package br.eti.rafaelcouto.mygympal.domain.mapper

import br.eti.rafaelcouto.mygympal.data.structure.CircularLinkedList
import br.eti.rafaelcouto.mygympal.domain.model.Grupo
import javax.inject.Inject

class GrupoMapper @Inject constructor() : GrupoMapperAbs {

    override fun map(input: List<Grupo>): CircularLinkedList<Grupo> {
        val linkedList = CircularLinkedList<Grupo>()
        linkedList.addAll(input)
        return linkedList
    }
}
