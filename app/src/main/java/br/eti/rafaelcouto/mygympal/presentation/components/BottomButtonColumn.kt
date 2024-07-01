package br.eti.rafaelcouto.mygympal.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BottomButtonColumn(
    buttonText: String,
    isButtonEnabled: Boolean = true,
    onButtonClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(
                    state = rememberScrollState()
                ),
            content = content
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            BottomButton(
                text = buttonText,
                isEnabled = isButtonEnabled,
                onClick = onButtonClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomButtonColumnEnabledPreview() {
    BottomButtonColumn(
        buttonText = "Salvar",
        content = {
            Text(text = "Hello, world!")
            Text(text = "Hello, again!")
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun BottomButtonColumnDisabledPreview() {
    BottomButtonColumn(
        buttonText = "Salvar",
        isButtonEnabled = false,
        content = {
            Text(text = "Hello, world!")
            Text(text = "Hello, again!")
        }
    )
}
