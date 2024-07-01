package br.eti.rafaelcouto.mygympal.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.eti.rafaelcouto.mygympal.R

@Composable
fun RoundedButton(
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier.size(size = dimensionResource(id = R.dimen.circle_button_size)),
        onClick = onClick,
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.colorPrimary),
            disabledContainerColor = colorResource(id = R.color.colorPrimaryAlpha),
            contentColor = colorResource(id = R.color.white),
            disabledContentColor = colorResource(id = R.color.white)
        ),
        content = {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription
            )
        }
    )
}

@Preview
@Composable
private fun RoundedButtonPreview() {
    RoundedButton(
        icon = Icons.Filled.Edit,
        contentDescription = "Editar"
    )
}
