package br.eti.rafaelcouto.gymbro.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import br.eti.rafaelcouto.gymbro.data.db.dao.ExerciseDao
import br.eti.rafaelcouto.gymbro.data.db.dao.WorkoutDao
import br.eti.rafaelcouto.gymbro.domain.model.Exercise
import br.eti.rafaelcouto.gymbro.domain.model.Workout

@Database(
    entities = [Workout::class, Exercise::class],
    exportSchema = false,
    version = 1
)
abstract class GymBroDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
}
