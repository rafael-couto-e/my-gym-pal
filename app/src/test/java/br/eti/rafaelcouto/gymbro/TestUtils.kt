package br.eti.rafaelcouto.gymbro

import br.eti.rafaelcouto.gymbro.domain.model.Exercise
import br.eti.rafaelcouto.gymbro.domain.model.Workout

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
}
