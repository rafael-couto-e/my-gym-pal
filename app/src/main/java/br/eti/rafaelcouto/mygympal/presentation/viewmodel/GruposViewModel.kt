package br.eti.rafaelcouto.mygympal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import br.eti.rafaelcouto.mygympal.domain.usecase.GrupoUseCaseAbs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GruposViewModel @Inject constructor(
    useCase: GrupoUseCaseAbs
) : ViewModel() {

    val grupos = useCase.listaGrupos()
}
