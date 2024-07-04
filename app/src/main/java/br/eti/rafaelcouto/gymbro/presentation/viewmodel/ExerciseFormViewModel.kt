package br.eti.rafaelcouto.gymbro.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.eti.rafaelcouto.gymbro.R
import br.eti.rafaelcouto.gymbro.domain.model.Exercise
import br.eti.rafaelcouto.gymbro.domain.usecase.ExerciseUseCaseAbs
import br.eti.rafaelcouto.gymbro.extensions.toIntOrZero
import br.eti.rafaelcouto.gymbro.navigation.exerciseIdArg
import br.eti.rafaelcouto.gymbro.navigation.workoutIdArg
import br.eti.rafaelcouto.gymbro.presentation.uistate.ExerciseFormUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseFormViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCase: ExerciseUseCaseAbs
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExerciseFormUiState())
    val uiState
        get() = _uiState.asStateFlow()

    init {
        _uiState.update { state ->
            state.copy(
                onExerciseNameChange = { exerciseName ->
                    _uiState.update {
                        it.copy(exerciseName = exerciseName)
                    }
                    updateButtonState()
                },
                onNumberOfSetsChange = { numberOfSets ->
                    _uiState.update {
                        it.copy(numberOfSets = numberOfSets)
                    }
                    updateButtonState()
                },
                onMinRepsChange = { minReps ->
                    _uiState.update {
                        it.copy(minReps = minReps)
                    }
                    updateButtonState()
                },
                onMaxRepsChange = { maxReps ->
                    _uiState.update {
                        it.copy(maxReps = maxReps)
                    }
                    updateButtonState()
                },
                onLoadChange = { load ->
                    _uiState.update {
                        it.copy(load = load)
                    }
                    updateButtonState()
                }
            )
        }
    }

    fun loadContent() {
        val exerciseId: Long = savedStateHandle.get<Long>(exerciseIdArg)?.takeIf { it != 0L } ?: return

        viewModelScope.launch {
            val exercise = useCase.findExerciseById(exerciseId) ?: return@launch

            _uiState.update { state ->
                state.copy(
                    exerciseId = exerciseId,
                    exerciseName = exercise.name,
                    numberOfSets = exercise.sets.toString(),
                    minReps = exercise.minReps.toString(),
                    maxReps = exercise.maxReps.toString(),
                    load = exercise.load.toString(),
                    isButtonEnabled = true,
                    successMessage = R.string.exercise_updated
                )
            }
        }
    }

    fun saveExercise() {
        val workoutId: Long = requireNotNull(savedStateHandle[workoutIdArg])

        val exercise = with(_uiState.value) {
            Exercise(
                id = exerciseId,
                name = exerciseName,
                sets = numberOfSets.toByte(),
                minReps = minReps.toByte(),
                maxReps = maxReps.toByte(),
                load = load.toShort(),
                workoutId = workoutId
            )
        }

        viewModelScope.launch {
            if (_uiState.value.exerciseId == 0L)
                useCase.createExercise(exercise)
            else
                useCase.updateExercise(exercise)
        }
    }

    fun deleteExercise() {
        viewModelScope.launch {
            useCase.deleteExercise(_uiState.value.exerciseId)
        }
    }

    private fun updateButtonState() {
        val isButtonEnabled = with(_uiState.value) {
            exerciseName.isNotBlank()
                    && numberOfSets.toIntOrZero() > 0
                    && minReps.toIntOrZero() > 0
                    && maxReps.toIntOrZero() > 0
                    && maxReps.toIntOrZero() >= minReps.toIntOrZero()
                    && load.toIntOrZero() > 0
        }

        _uiState.update { state ->
            state.copy(isButtonEnabled = isButtonEnabled)
        }
    }
}
