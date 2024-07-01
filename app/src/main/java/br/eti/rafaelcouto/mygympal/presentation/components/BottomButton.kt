package br.eti.rafaelcouto.mygympal.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import br.eti.rafaelcouto.mygympal.R

@Composable
fun BottomButton(
    text: String,
    isEnabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_m)),
        onClick = onClick,
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.colorPrimary),
            disabledContainerColor = colorResource(id = R.color.colorPrimaryAlpha),
            contentColor = colorResource(id = R.color.white),
            disabledContentColor = colorResource(id = R.color.white)
        ),
        content = {
            Text(text = text)
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun BottomButtonEnabledPreview() {
    BottomButton(text = "Salvar")
}

@Preview(showBackground = true)
@Composable
private fun BottomButtonDisabledPreview() {
    BottomButton(
        text = "Salvar",
        isEnabled = false
    )
}
