package br.eti.rafaelcouto.gymbro.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EmptyMessage(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
        content = { Text(text = text) }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun EmptyMessagePreview() {
    EmptyMessage(text = "Hello, world!")
}
