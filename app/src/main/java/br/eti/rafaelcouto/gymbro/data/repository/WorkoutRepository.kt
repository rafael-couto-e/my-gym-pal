package br.eti.rafaelcouto.gymbro.data.repository

import br.eti.rafaelcouto.gymbro.data.db.dao.WorkoutDao
import br.eti.rafaelcouto.gymbro.domain.model.Workout
import javax.inject.Inject

class WorkoutRepository @Inject constructor(
    private val dao: WorkoutDao
) : WorkoutRepositoryAbs {

    override fun getAllWorkouts() = dao.getAllWorkouts()
    override fun findWorkoutByIdAsFlow(id: Long) = dao.findWorkoutByIdAsFlow(id)
    override suspend fun findWorkoutById(id: Long) = dao.findWorkoutById(id)
    override suspend fun createWorkout(workout: Workout) = dao.createWorkout(workout)
    override suspend fun updateWorkout(workout: Workout) = dao.updateWorkout(workout)
    override suspend fun deleteWorkout(id: Long) = dao.deleteWorkout(id)
}
