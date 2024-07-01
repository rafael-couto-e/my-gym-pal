package br.eti.rafaelcouto.mygympal.presentation.uistate

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import br.eti.rafaelcouto.mygympal.R

data class MainActivityUiState(
    val title: String? = null,
    @StringRes val titleRes: Int = R.string.app_name,
    val showsBackButton: Boolean = false,
    val floatingActionButton: @Composable () -> Unit = {},
    val topAppBarActions: @Composable RowScope.() -> Unit = {}
)
