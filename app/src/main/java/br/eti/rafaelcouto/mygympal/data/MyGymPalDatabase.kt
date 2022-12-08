package br.eti.rafaelcouto.mygympal.data

import androidx.room.Database
import androidx.room.RoomDatabase
import br.eti.rafaelcouto.mygympal.data.source.ExercicioDao
import br.eti.rafaelcouto.mygympal.data.source.GrupoDao
import br.eti.rafaelcouto.mygympal.domain.model.Exercicio
import br.eti.rafaelcouto.mygympal.domain.model.Grupo

@Database(entities = [Grupo::class, Exercicio::class], version = 1)
abstract class MyGymPalDatabase : RoomDatabase() {
    abstract fun grupoDao(): GrupoDao
    abstract fun exercicioDao(): ExercicioDao
}
