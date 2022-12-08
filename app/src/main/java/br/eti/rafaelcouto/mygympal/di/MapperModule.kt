package br.eti.rafaelcouto.mygympal.di

import br.eti.rafaelcouto.mygympal.domain.mapper.ExercicioMapper
import br.eti.rafaelcouto.mygympal.domain.mapper.ExercicioMapperAbs
import br.eti.rafaelcouto.mygympal.domain.mapper.GrupoMapper
import br.eti.rafaelcouto.mygympal.domain.mapper.GrupoMapperAbs
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MapperModule {

    @Binds abstract fun bindGrupoMapper(implementation: GrupoMapper): GrupoMapperAbs
    @Binds abstract fun bindExercicioMapper(implementation: ExercicioMapper): ExercicioMapperAbs
}
