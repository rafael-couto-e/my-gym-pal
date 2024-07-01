package br.eti.rafaelcouto.mygympal.data.repository

import br.eti.rafaelcouto.mygympal.data.db.dao.ExerciseDao
import br.eti.rafaelcouto.mygympal.domain.model.Exercise
import javax.inject.Inject

class ExerciseRepository @Inject constructor(
    private val dao: ExerciseDao
) : ExerciseRepositoryAbs {

    override fun getAllExercises(workoutId: Long) = dao.getAllExercises(workoutId)
    override suspend fun findExerciseById(id: Long) = dao.findExerciseById(id)
    override suspend fun createExercise(exercise: Exercise) = dao.createExercise(exercise)
    override suspend fun updateExercise(exercise: Exercise) = dao.updateExercise(exercise)
    override suspend fun deleteExercise(id: Long) = dao.deleteExercise(id)
}
