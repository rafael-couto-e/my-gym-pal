package br.eti.rafaelcouto.gymbro.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import br.eti.rafaelcouto.gymbro.R

@Composable
fun TextField(
    value: String,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    imeAction: ImeAction = ImeAction.Default,
    onValueChange: (String) -> Unit = {}
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.padding_m),
                vertical = dimensionResource(id = R.dimen.padding_p)
            ),
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            capitalization = capitalization,
            imeAction = imeAction
        ),
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = colorResource(id = R.color.colorSecondary),
            selectionColors = TextSelectionColors(
                handleColor = colorResource(id = R.color.darkGrey),
                backgroundColor = colorResource(id = R.color.mediumGrey)
            ),
            focusedTextColor = colorResource(id = R.color.black),
            unfocusedTextColor = colorResource(id = R.color.black),
            focusedLabelColor = colorResource(id = R.color.colorPrimary),
            unfocusedLabelColor = colorResource(id = R.color.black),
            focusedBorderColor = colorResource(id = R.color.colorPrimary),
            unfocusedBorderColor = colorResource(id = R.color.black)
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun TextFieldFilledPreview() {
    TextField(
        value = "Value",
        label = "Filled"
    )
}

@Preview(showBackground = true)
@Composable
private fun TextFieldNotFilledPreview() {
    TextField(
        value = "",
        label = "Not Filled"
    )
}
