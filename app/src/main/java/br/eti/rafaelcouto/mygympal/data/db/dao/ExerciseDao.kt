package br.eti.rafaelcouto.mygympal.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.eti.rafaelcouto.mygympal.domain.model.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM Exercise WHERE workoutId = :workoutId")
    fun getAllExercises(workoutId: Long): Flow<List<Exercise>>

    @Query("SELECT * FROM Exercise WHERE id = :id")
    suspend fun findExerciseById(id: Long): Exercise?

    @Insert
    suspend fun createExercise(exercise: Exercise)

    @Update
    suspend fun updateExercise(exercise: Exercise)
    @Query("DELETE FROM Exercise WHERE id = :id")
    suspend fun deleteExercise(id: Long)
}
