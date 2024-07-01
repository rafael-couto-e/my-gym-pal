package br.eti.rafaelcouto.mygympal.domain.usecase

import br.eti.rafaelcouto.mygympal.data.repository.ExerciseRepositoryAbs
import br.eti.rafaelcouto.mygympal.domain.mapper.ExerciseMapperAbs
import br.eti.rafaelcouto.mygympal.domain.model.Exercise
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExerciseUseCase @Inject constructor(
    private val repository: ExerciseRepositoryAbs,
    private val mapper: ExerciseMapperAbs
) : ExerciseUseCaseAbs {

    override fun getAllExercises(workoutId: Long) = repository.getAllExercises(workoutId).map {
        mapper.map(it)
    }

    override suspend fun findExerciseById(id: Long) = repository.findExerciseById(id)
    override suspend fun createExercise(exercise: Exercise) = repository.createExercise(exercise)
    override suspend fun updateExercise(exercise: Exercise) = repository.updateExercise(exercise)
    override suspend fun deleteExercise(id: Long) = repository.deleteExercise(id)

    override suspend fun increaseLoad(exercise: Exercise) {
        repository.updateExercise(
            exercise.copy(
                load = exercise.load.inc()
            )
        )
    }

    override suspend fun decreaseLoad(exercise: Exercise) {
        repository.updateExercise(
            exercise.copy(
                load = exercise.load.dec()
            )
        )
    }
}
