package br.eti.rafaelcouto.mygympal.di

import br.eti.rafaelcouto.mygympal.data.repository.ExercicioRepository
import br.eti.rafaelcouto.mygympal.data.repository.ExercicioRepositoryAbs
import br.eti.rafaelcouto.mygympal.data.repository.GrupoRepository
import br.eti.rafaelcouto.mygympal.data.repository.GrupoRepositoryAbs
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds abstract fun bindGrupoRepository(implementation: GrupoRepository): GrupoRepositoryAbs
    @Binds abstract fun bindExercicioRepository(implementation: ExercicioRepository): ExercicioRepositoryAbs
}
