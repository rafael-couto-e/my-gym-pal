package br.eti.rafaelcouto.mygympal.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.eti.rafaelcouto.mygympal.domain.model.Exercise
import br.eti.rafaelcouto.mygympal.domain.usecase.ExerciseUseCaseAbs
import br.eti.rafaelcouto.mygympal.domain.usecase.WorkoutUseCaseAbs
import br.eti.rafaelcouto.mygympal.navigation.workoutIdArg
import br.eti.rafaelcouto.mygympal.presentation.uistate.ExerciseListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val exerciseUseCase: ExerciseUseCaseAbs,
    private val workoutUseCase: WorkoutUseCaseAbs
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExerciseListUiState())
    val uiState
        get() = _uiState.asStateFlow()

    val workoutId: Long = requireNotNull(savedStateHandle[workoutIdArg])

    init {
        loadExercises()
        loadWorkout()
    }

    fun increaseLoad(exercise: Exercise) {
        viewModelScope.launch {
            exerciseUseCase.increaseLoad(exercise)
        }
    }

    fun decreaseLoad(exercise: Exercise) {
        viewModelScope.launch {
            exerciseUseCase.decreaseLoad(exercise)
        }
    }

    fun finishSet(exercise: Exercise.UI, set: Int) {
        _uiState.update { state ->
            state.copy(
                exercises = state.exercises.map { item ->
                    if (exercise.original.id == item.original.id)
                        item.copy(
                            setsState = item.setsState.mapIndexed { index, setState ->
                                if (index == set)
                                    !setState
                                else
                                    setState
                            }
                        )
                    else
                        item
                }
            )
        }

        updateWorkoutState()
    }

    fun deleteWorkout() {
        viewModelScope.launch {
            workoutUseCase.deleteWorkout(workoutId)
        }
    }

    fun finishWorkout() {
        updateLastWorkout()
        finishAllExercises()
    }

    fun setMenuState(isMenuExpanded: Boolean) {
        _uiState.update { state ->
            state.copy(isMenuExpanded = isMenuExpanded)
        }
    }

    private fun loadExercises() {
        viewModelScope.launch {
            exerciseUseCase.getAllExercises(workoutId).collect { exercises ->
                _uiState.update { state ->
                    state.copy(
                        exercises = exercises
                    )
                }
            }
        }
    }

    private fun loadWorkout() {
        viewModelScope.launch {
            val workout = workoutUseCase.getWorkoutById(workoutId) ?: return@launch

            _uiState.update { state ->
                state.copy(workout = workout)
            }
        }
    }

    private fun updateWorkoutState() {
        val allFinished = _uiState.value.exercises.all { it.finished }

        _uiState.update { state ->
            state.copy(
                canFinishWorkout = !allFinished
            )
        }

        if (allFinished)
            updateLastWorkout()
    }

    private fun updateLastWorkout() {
        viewModelScope.launch {
            workoutUseCase.finishWorkout(workoutId)
        }
    }

    private fun finishAllExercises() {
        _uiState.update { state ->
            state.copy(
                exercises = state.exercises.map { exercise ->
                    exercise.copy(
                        setsState = MutableList(exercise.original.sets.toInt()) { true }.toList()
                    )
                },
                canFinishWorkout = false
            )
        }
    }
}
