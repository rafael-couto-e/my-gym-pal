package br.eti.rafaelcouto.gymbro.extensions

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import br.eti.rafaelcouto.gymbro.data.structure.CircularLinkedList

fun <T> LazyListScope.items(
    collection: CircularLinkedList<T>,
    itemContent: @Composable LazyItemScope.(item: T) -> Unit
) {
    items(collection.size) { index ->
        val item = collection[index].value
        itemContent(item)
    }
}
