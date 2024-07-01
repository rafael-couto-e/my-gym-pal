package br.eti.rafaelcouto.mygympal.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import br.eti.rafaelcouto.mygympal.data.db.dao.ExerciseDao
import br.eti.rafaelcouto.mygympal.data.db.dao.WorkoutDao
import br.eti.rafaelcouto.mygympal.domain.model.Exercise
import br.eti.rafaelcouto.mygympal.domain.model.Workout

@Database(
    entities = [Workout::class, Exercise::class],
    exportSchema = false,
    version = 1
)
abstract class MyGymPalDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
}
