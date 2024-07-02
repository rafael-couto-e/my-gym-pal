package br.eti.rafaelcouto.gymbro.di

import br.eti.rafaelcouto.gymbro.domain.usecase.ExerciseUseCase
import br.eti.rafaelcouto.gymbro.domain.usecase.ExerciseUseCaseAbs
import br.eti.rafaelcouto.gymbro.domain.usecase.WorkoutUseCase
import br.eti.rafaelcouto.gymbro.domain.usecase.WorkoutUseCaseAbs
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds abstract fun bindWorkoutUseCase(implementation: WorkoutUseCase): WorkoutUseCaseAbs
    @Binds abstract fun bindExerciseUseCase(implementation: ExerciseUseCase): ExerciseUseCaseAbs
}
