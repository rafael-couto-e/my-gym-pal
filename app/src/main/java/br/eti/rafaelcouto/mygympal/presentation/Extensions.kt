package br.eti.rafaelcouto.mygympal.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import br.eti.rafaelcouto.mygympal.domain.model.Exercicio
import kotlinx.coroutines.flow.Flow

fun String.toIntOrZero(): Int {
    return try {
        this.toInt()
    } catch (e: Exception) {
        0
    }
}

@Composable
fun <T> Flow<T>.collectAsStateWithLifecycle(owner: LifecycleOwner, initial: T): T {
    val awareFlow = remember(this, owner) {
        this.flowWithLifecycle(owner.lifecycle, Lifecycle.State.STARTED)
    }
    val content: T by awareFlow.collectAsState(initial = initial)
    return content
}
