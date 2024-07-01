package br.eti.rafaelcouto.mygympal.di

import android.content.Context
import androidx.room.Room
import br.eti.rafaelcouto.mygympal.data.db.MyGymPalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyGymPalModule {

    @Singleton
    @Provides
    fun provideMyGymPalDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, MyGymPalDatabase::class.java, "my_gym_pal").build()

    @Singleton
    @Provides
    fun provideWorkoutDao(database: MyGymPalDatabase) = database.workoutDao()

    @Singleton
    @Provides
    fun provideExerciseDao(database: MyGymPalDatabase) = database.exerciseDao()
}
