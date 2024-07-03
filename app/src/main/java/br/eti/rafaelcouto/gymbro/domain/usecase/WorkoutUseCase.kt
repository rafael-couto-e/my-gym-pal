package br.eti.rafaelcouto.gymbro.domain.usecase

import br.eti.rafaelcouto.gymbro.data.repository.WorkoutRepositoryAbs
import br.eti.rafaelcouto.gymbro.domain.mapper.WorkoutMapperAbs
import br.eti.rafaelcouto.gymbro.domain.model.Workout
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WorkoutUseCase @Inject constructor(
    private val repository: WorkoutRepositoryAbs,
    private val mapper: WorkoutMapperAbs
) : WorkoutUseCaseAbs {

    override fun getAllWorkouts() = repository.getAllWorkouts().map {
        mapper.map(it)
    }

    override fun getWorkoutByIdAsFlow(id: Long) = repository.findWorkoutByIdAsFlow(id)
    override suspend fun getWorkoutById(id: Long) = repository.findWorkoutById(id)
    override suspend fun createWorkout(workout: Workout) = repository.createWorkout(workout)
    override suspend fun updateWorkout(workout: Workout) = repository.updateWorkout(workout)
    override suspend fun deleteWorkout(id: Long) {
        val node = getAllWorkouts().map { workouts ->
            workouts.first { it.id == id }
        }.first()

        if (node.value.isLast) {
            val newLast = node.next?.value
            newLast?.let { workout ->
                updateWorkout(workout.copy(isLast = true))
            }
        }

        repository.deleteWorkout(id)
    }

    override suspend fun finishWorkout(id: Long) {
        val workouts = getAllWorkouts().first()

        val finishedWorkout = workouts.firstOrNull { it.id == id } ?: return
        val nextWorkout = finishedWorkout.next?.value ?: return
        val currentLastWorkout = workouts.first { it.isLast }.value

        if (currentLastWorkout.id == nextWorkout.id) return

        updateWorkout(nextWorkout.copy(isLast = true))
        updateWorkout(currentLastWorkout.copy(isLast = false))
    }
}
