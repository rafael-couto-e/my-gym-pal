package br.eti.rafaelcouto.gymbro.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.eti.rafaelcouto.gymbro.R

@Composable
fun DropDownMenu(
    expanded: Boolean = false,
    onToggle: (Boolean) -> Unit = {},
    items: @Composable ColumnScope.() -> Unit
) {
    IconButton(onClick = { onToggle(!expanded) }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(id = R.string.options)
        )
    }

    DropdownMenu(
        modifier = Modifier.background(color = colorResource(id = R.color.lightGrey)),
        expanded = expanded,
        onDismissRequest = { onToggle(false) },
        content = items
    )
}

@Preview(showBackground = true)
@Composable
private fun DropDownMenuCollapsedPreview() {
    DropDownMenu {
        DropdownMenuItem(
            text = {
                Text("Item 1")
            },
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DropDownMenuExpandedPreview() {
    DropDownMenu(expanded = true) {
        DropdownMenuItem(
            text = {
                Text(text = "Item 1")
            },
            onClick = {}
        )
    }
}
