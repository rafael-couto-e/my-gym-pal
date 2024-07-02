package br.eti.rafaelcouto.gymbro.data.structure

data class Node<T>(
    val value: T,
    var next: Node<T>? = null
)
