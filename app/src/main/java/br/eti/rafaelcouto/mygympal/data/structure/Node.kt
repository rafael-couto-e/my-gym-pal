package br.eti.rafaelcouto.mygympal.data.structure

data class Node<T>(
    val value: T,
    var next: Node<T>? = null
)
