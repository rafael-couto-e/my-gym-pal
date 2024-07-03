package br.eti.rafaelcouto.gymbro.data.structure

class CircularLinkedList<T> {

    private val content = mutableListOf<Node<T>>()

    val size: Int get() = content.size

    fun add(element: T) {
        val node = Node(element)

        content.lastOrNull()?.let {
            it.next = node
            node.next = content.firstOrNull()
        }

        content.add(node)
    }

    fun addAll(collection: List<T>) = collection.forEach(::add)

    fun add(index: Int, element: T) {
        if (index < 0) {
            add(0, element)
            return
        }

        if (index >= content.size) {
            add(element)
            return
        }

        var node = Node(element)

        content.add(index, node)

        if (index != 0) {
            val previous = content[index - 1]
            previous.next = node
        }

        for (i in index until content.size) {
            val current = content[i]

            node.next = current
            node = current
        }

        node.next = content.firstOrNull()
    }

    fun remove(element: T): Boolean {
        val index = content.indexOfFirst { it.value == element }
        return remove(index)
    }

    fun remove(index: Int): Boolean {
        if (index < 0 || index >= content.size) return false

        if (index == 0 || index == content.size - 1) {
            content.removeAt(index)

            content.lastOrNull()?.next = if (content.size != 1)
                content.firstOrNull()
            else
                null

            return true
        }

        val previous = content[index - 1]
        previous.next = content[index + 1]

        content.removeAt(index)

        return true
    }

    fun first(predicate: (T) -> Boolean) = content.first { predicate(it.value) }
    fun firstOrNull(predicate: (T) -> Boolean) = content.firstOrNull { predicate(it.value) }
    fun filter(predicate: (T) -> Boolean) = content.filter { predicate(it.value) }
    fun isEmpty() = content.isEmpty()

    operator fun get(index: Int) = content[index]
}
