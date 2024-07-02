package br.eti.rafaelcouto.gymbro.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import br.eti.rafaelcouto.gymbro.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

@Composable
fun FloatingActionButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit = {}
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = colorResource(id = R.color.colorPrimary),
        contentColor = colorResource(id = R.color.white),
        content = {
            Icon(imageVector = icon, contentDescription = contentDescription)
        }
    )
}

@Preview
@Composable
private fun FloatingActionButtonPreview() {
    FloatingActionButton(
        icon = Icons.Filled.Add,
        contentDescription = "Incluir"
    )
}
