package br.eti.rafaelcouto.gymbro

import android.support.annotation.StringRes
import br.eti.rafaelcouto.gymbro.domain.model.Exercise
import br.eti.rafaelcouto.gymbro.domain.model.Workout
import br.eti.rafaelcouto.gymbro.presentation.uistate.ExerciseFormUiState
import br.eti.rafaelcouto.gymbro.presentation.uistate.ExerciseListUiState
import br.eti.rafaelcouto.gymbro.presentation.uistate.WorkoutFormScreenUiState

object TestUtils {
    fun generateWorkout(
        id: Long = 0,
        name: String = "Workout $id",
        isLast: Boolean = false
    ) = Workout(id, name, isLast)

    fun generateExercise(
        id: Long = 0,
        name: String = "Exercise $id",
        sets: Byte = 3,
        minReps: Byte = 8,
        maxReps: Byte = 12,
        load: Short = 20,
        workoutId: Long = 0
    ) = Exercise(id, name, sets, minReps, maxReps, load, workoutId)

    fun generateExerciseUi(
        id: Long = 0,
        name: String = "Exercise $id",
        sets: Byte = 3,
        minReps: Byte = 8,
        maxReps: Byte = 12,
        load: Short = 20,
        workoutId: Long = 0,
        setsState: List<Boolean> = MutableList(sets.toInt()) { false }.toList()
    ) = Exercise.UI(
        Exercise(id, name, sets, minReps, maxReps, load, workoutId),
        setsState
    )

    fun generateWorkoutFormScreenUiState(
        workout: Workout = generateWorkout(),
        isButtonEnabled: Boolean = false,
        @StringRes successMessage: Int = R.string.workout_created
    ) = WorkoutFormScreenUiState(
        workoutId = workout.id,
        workoutName = workout.name,
        isButtonEnabled = isButtonEnabled,
        successMessage = successMessage
    )

    fun generateExerciseListUiState(
        isMenuExpanded: Boolean = false,
        workout: Workout = generateWorkout(),
        exercises: List<Exercise.UI> = emptyList(),
        canFinishWorkout: Boolean = true
    ) = ExerciseListUiState(
        isMenuExpanded = isMenuExpanded,
        workout = workout,
        exercises = exercises,
        canFinishWorkout = canFinishWorkout
    )

    fun generateExerciseFormUiState(
        exercise: Exercise = generateExercise(),
        isButtonEnabled: Boolean = false,
        @StringRes successMessage: Int = R.string.exercise_created
    ) = ExerciseFormUiState(
        exerciseId = exercise.id,
        exerciseName = exercise.name,
        numberOfSets = exercise.sets.toString(),
        minReps = exercise.minReps.toString(),
        maxReps = exercise.maxReps.toString(),
        load = exercise.load.toString(),
        isButtonEnabled = isButtonEnabled,
        successMessage = successMessage
    )
}
