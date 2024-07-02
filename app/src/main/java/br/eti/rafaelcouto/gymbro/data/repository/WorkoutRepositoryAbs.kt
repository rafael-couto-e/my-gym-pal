package br.eti.rafaelcouto.gymbro.data.repository

import br.eti.rafaelcouto.gymbro.domain.model.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutRepositoryAbs {
    fun getAllWorkouts(): Flow<List<Workout>>
    fun findWorkoutByIdAsFlow(id: Long): Flow<Workout?>
    suspend fun findWorkoutById(id: Long): Workout?
    suspend fun createWorkout(workout: Workout)
    suspend fun updateWorkout(workout: Workout)
    suspend fun deleteWorkout(id: Long)
}
