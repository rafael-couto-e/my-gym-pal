package br.eti.rafaelcouto.mygympal.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.eti.rafaelcouto.mygympal.domain.model.Workout
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM Workout")
    fun getAllWorkouts(): Flow<List<Workout>>

    @Query("SELECT * FROM Workout WHERE Workout.id = :id")
    suspend fun findWorkoutById(id: Long): Workout?

    @Insert
    suspend fun createWorkout(workout: Workout)

    @Update
    suspend fun updateWorkout(workout: Workout)

    @Query("DELETE FROM Workout WHERE id = :id")
    suspend fun deleteWorkout(id: Long)
}
