package br.eti.rafaelcouto.gymbro.domain.usecase

import br.eti.rafaelcouto.gymbro.domain.model.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseUseCaseAbs {

    fun getAllExercises(workoutId: Long): Flow<List<Exercise.UI>>
    suspend fun findExerciseById(id: Long): Exercise?
    suspend fun createExercise(exercise: Exercise)
    suspend fun updateExercise(exercise: Exercise)
    suspend fun deleteExercise(id: Long)
    suspend fun increaseLoad(exercise: Exercise)
    suspend fun decreaseLoad(exercise: Exercise)
}
