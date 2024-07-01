package br.eti.rafaelcouto.mygympal.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.eti.rafaelcouto.mygympal.domain.model.Workout
import br.eti.rafaelcouto.mygympal.domain.usecase.WorkoutUseCaseAbs
import br.eti.rafaelcouto.mygympal.navigation.workoutIdArg
import br.eti.rafaelcouto.mygympal.presentation.uistate.WorkoutFormScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutFormViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCase: WorkoutUseCaseAbs
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutFormScreenUiState())
    val uiState
        get() = _uiState.asStateFlow()

    init {
        _uiState.update { state ->
            state.copy(
                onWorkoutNameChanged = { workoutName ->
                    _uiState.update {
                        it.copy(
                            workoutName = workoutName,
                            isButtonEnabled = workoutName.isNotBlank()
                        )
                    }
                }
            )
        }

        getWorkout()
    }

    fun saveWorkout() {
        viewModelScope.launch {
            with (_uiState.value) {
                if (workoutId != 0L) {
                    val workout = useCase.getWorkoutById(workoutId)
                        ?.copy(name = workoutName) ?: return@launch

                    useCase.updateWorkout(workout)
                } else {
                    val lastWorkout = useCase.getAllWorkouts().map { workouts ->
                        workouts.filter { it.value.isLast }.firstOrNull()
                    }.first()

                    val workout = Workout(
                        name = workoutName.trim(),
                        isLast = lastWorkout == null
                    )

                    useCase.createWorkout(workout)
                }
            }
        }
    }

    private fun getWorkout() {
        val workoutId: Long = savedStateHandle[workoutIdArg] ?: return

        viewModelScope.launch {
            val workout = useCase.getWorkoutById(workoutId) ?: return@launch

            _uiState.update {
                it.copy(
                    workoutId = workout.id,
                    workoutName = workout.name,
                    isButtonEnabled = true
                )
            }
        }
    }
}
