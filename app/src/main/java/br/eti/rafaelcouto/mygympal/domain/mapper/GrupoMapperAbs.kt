package br.eti.rafaelcouto.mygympal.domain.mapper

import br.eti.rafaelcouto.mygympal.data.structure.CircularLinkedList
import br.eti.rafaelcouto.mygympal.domain.model.Grupo

interface GrupoMapperAbs {

    fun map(input: List<Grupo>): CircularLinkedList<Grupo>
}
