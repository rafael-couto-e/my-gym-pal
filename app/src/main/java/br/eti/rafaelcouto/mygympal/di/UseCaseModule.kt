package br.eti.rafaelcouto.mygympal.di

import br.eti.rafaelcouto.mygympal.domain.usecase.ExercicioUseCase
import br.eti.rafaelcouto.mygympal.domain.usecase.ExercicioUseCaseAbs
import br.eti.rafaelcouto.mygympal.domain.usecase.GrupoUseCase
import br.eti.rafaelcouto.mygympal.domain.usecase.GrupoUseCaseAbs
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds abstract fun bindGrupoUseCase(implementation: GrupoUseCase): GrupoUseCaseAbs
    @Binds abstract fun bindExercicioUseCase(implementation: ExercicioUseCase): ExercicioUseCaseAbs
}
