package br.eti.rafaelcouto.mygympal.domain.usecase

import br.eti.rafaelcouto.mygympal.data.structure.CircularLinkedList
import br.eti.rafaelcouto.mygympal.domain.model.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutUseCaseAbs {

    fun getAllWorkouts(): Flow<CircularLinkedList<Workout>>
    suspend fun getWorkoutById(id: Long): Workout?
    suspend fun createWorkout(workout: Workout)
    suspend fun updateWorkout(workout: Workout)
    suspend fun deleteWorkout(id: Long)
    suspend fun finishWorkout(id: Long)
}
