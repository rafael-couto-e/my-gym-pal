package br.eti.rafaelcouto.gymbro.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.eti.rafaelcouto.gymbro.R
import br.eti.rafaelcouto.gymbro.domain.model.Workout
import br.eti.rafaelcouto.gymbro.domain.usecase.WorkoutUseCaseAbs
import br.eti.rafaelcouto.gymbro.navigation.workoutIdArg
import br.eti.rafaelcouto.gymbro.presentation.uistate.WorkoutFormScreenUiState
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
    }

    fun loadContent() {
        val workoutId = savedStateHandle.get<Long>(workoutIdArg)?.takeIf { it != 0L } ?: return

        viewModelScope.launch {
            val workout = useCase.getWorkoutById(workoutId) ?: return@launch

            _uiState.update {
                it.copy(
                    workoutId = workout.id,
                    workoutName = workout.name,
                    isButtonEnabled = true,
                    successMessage = R.string.workout_updated
                )
            }
        }
    }

    fun saveWorkout() {
        if (_uiState.value.workoutId != 0L)
            updateWorkout()
        else
            createWorkout()
    }

    private fun updateWorkout() {
        viewModelScope.launch {
            val workout = useCase.getWorkoutById(_uiState.value.workoutId)
                ?.copy(name = _uiState.value.workoutName) ?: return@launch

            useCase.updateWorkout(workout)
        }
    }

    private fun createWorkout() {
        viewModelScope.launch {
            val lastWorkout = useCase.getAllWorkouts().map { workouts ->
                workouts.filter { it.isLast }.firstOrNull()
            }.first()

            val workout = Workout(
                name = _uiState.value.workoutName.trim(),
                isLast = lastWorkout == null
            )

            useCase.createWorkout(workout)
        }
    }
}
