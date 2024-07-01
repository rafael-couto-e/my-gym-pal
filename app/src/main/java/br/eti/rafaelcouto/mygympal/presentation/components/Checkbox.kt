package br.eti.rafaelcouto.mygympal.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import br.eti.rafaelcouto.mygympal.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Checkbox(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Checkbox(
            modifier = modifier
                .padding(vertical = dimensionResource(id = R.dimen.padding_p)),
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = CheckboxDefaults.colors(
                checkedColor = colorResource(id = R.color.success),
                disabledCheckedColor = colorResource(id = R.color.successAlpha),
                uncheckedColor = colorResource(id = R.color.colorPrimary),
                checkmarkColor = colorResource(id = R.color.white)
            )
        )
    }
}

@Preview
@Composable
private fun CheckboxUncheckedEnabledPreview() {
    Checkbox()
}

@Preview
@Composable
private fun CheckboxCheckedEnabledPreview() {
    Checkbox(
        checked = true
    )
}

@Preview
@Composable
private fun CheckboxCheckedDisabledPreview() {
    Checkbox(
        checked = true,
        enabled = false
    )
}
