package br.eti.rafaelcouto.mygympal.data.repository

import br.eti.rafaelcouto.mygympal.domain.model.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutRepositoryAbs {
    fun getAllWorkouts(): Flow<List<Workout>>
    suspend fun findWorkoutById(id: Long): Workout?
    suspend fun createWorkout(workout: Workout)
    suspend fun updateWorkout(workout: Workout)
    suspend fun deleteWorkout(id: Long)
}
