package br.eti.rafaelcouto.gymbro.di

import android.content.Context
import androidx.room.Room
import br.eti.rafaelcouto.gymbro.data.db.GymBroDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GymBroModule {

    @Singleton
    @Provides
    fun provideGymBroDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, GymBroDatabase::class.java, "gym_bro.db").build()

    @Singleton
    @Provides
    fun provideWorkoutDao(database: GymBroDatabase) = database.workoutDao()

    @Singleton
    @Provides
    fun provideExerciseDao(database: GymBroDatabase) = database.exerciseDao()
}
