package br.eti.rafaelcouto.mygympal.domain.usecase

import br.eti.rafaelcouto.mygympal.data.repository.WorkoutRepositoryAbs
import br.eti.rafaelcouto.mygympal.domain.mapper.WorkoutMapperAbs
import br.eti.rafaelcouto.mygympal.domain.model.Workout
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

    override suspend fun getWorkoutById(id: Long) = repository.findWorkoutById(id)
    override suspend fun createWorkout(workout: Workout) = repository.createWorkout(workout)
    override suspend fun updateWorkout(workout: Workout) = repository.updateWorkout(workout)
    override suspend fun deleteWorkout(id: Long) {
        val node = getAllWorkouts().map { workouts ->
            workouts.first { it.value.id == id }
        }.first()

        if (node.value.isLast) {
            val newLast = node.next?.value
            newLast?.let {
                it.isLast = true
                updateWorkout(it)
            }
        }

        repository.deleteWorkout(id)
    }

    override suspend fun finishWorkout(id: Long) {
        val workouts = getAllWorkouts().first()

        val workout = workouts.firstOrNull { it.value.id == id } ?: return
        val last = workouts.first { it.value.isLast }

        workout.value.isLast = false
        last.value.isLast = false
        workout.next?.value?.isLast = true

        updateWorkout(workout.value)

        workout.next?.value?.let {
            updateWorkout(it)
        }

        updateWorkout(last.value)
    }
}
