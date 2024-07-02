package br.eti.rafaelcouto.gymbro.di

import br.eti.rafaelcouto.gymbro.domain.mapper.ExerciseMapper
import br.eti.rafaelcouto.gymbro.domain.mapper.ExerciseMapperAbs
import br.eti.rafaelcouto.gymbro.domain.mapper.WorkoutMapper
import br.eti.rafaelcouto.gymbro.domain.mapper.WorkoutMapperAbs
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MapperModule {

    @Binds abstract fun bindWorkoutMapper(implementation: WorkoutMapper): WorkoutMapperAbs
    @Binds abstract fun bindExerciseMapper(implementation: ExerciseMapper): ExerciseMapperAbs
}
