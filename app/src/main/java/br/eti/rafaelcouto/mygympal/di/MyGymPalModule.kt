package br.eti.rafaelcouto.mygympal.di

import android.content.Context
import androidx.room.Room
import br.eti.rafaelcouto.mygympal.data.MyGymPalDatabase
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
    ) = Room.databaseBuilder(context, MyGymPalDatabase::class.java, "my_gym_pal").allowMainThreadQueries().build()

    @Singleton
    @Provides
    fun provideGrupoDao(database: MyGymPalDatabase) = database.grupoDao()

    @Singleton
    @Provides
    fun provideExercicioDao(database: MyGymPalDatabase) = database.exercicioDao()
}
