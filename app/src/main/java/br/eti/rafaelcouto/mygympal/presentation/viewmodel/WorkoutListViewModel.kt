package br.eti.rafaelcouto.mygympal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.eti.rafaelcouto.mygympal.domain.usecase.WorkoutUseCaseAbs
import br.eti.rafaelcouto.mygympal.presentation.uistate.WorkoutListScrenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutListViewModel @Inject constructor(
    private val useCase: WorkoutUseCaseAbs
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutListScrenUiState())
    val uiState
        get() = _uiState.asStateFlow()

    init {
        getWorkouts()
    }

    private fun getWorkouts() {
        viewModelScope.launch {
            useCase.getAllWorkouts().collect { workouts ->
                _uiState.update { state ->
                    state.copy(
                        workouts = workouts
                    )
                }
            }
        }
    }
}
