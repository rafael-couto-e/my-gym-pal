package br.eti.rafaelcouto.gymbro.di

import br.eti.rafaelcouto.gymbro.data.repository.ExerciseRepository
import br.eti.rafaelcouto.gymbro.data.repository.ExerciseRepositoryAbs
import br.eti.rafaelcouto.gymbro.data.repository.WorkoutRepository
import br.eti.rafaelcouto.gymbro.data.repository.WorkoutRepositoryAbs
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds abstract fun bindWorkoutRepository(implementation: WorkoutRepository): WorkoutRepositoryAbs
    @Binds abstract fun bindExerciseRepository(implementation: ExerciseRepository): ExerciseRepositoryAbs
}
