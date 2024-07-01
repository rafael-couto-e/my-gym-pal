package br.eti.rafaelcouto.mygympal.data.repository

import br.eti.rafaelcouto.mygympal.domain.model.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseRepositoryAbs {

    fun getAllExercises(workoutId: Long): Flow<List<Exercise>>
    suspend fun findExerciseById(id: Long): Exercise?
    suspend fun createExercise(exercise: Exercise)
    suspend fun updateExercise(exercise: Exercise)
    suspend fun deleteExercise(id: Long)
}
