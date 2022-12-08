package br.eti.rafaelcouto.mygympal.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.eti.rafaelcouto.mygympal.domain.model.Exercicio

@Dao
interface ExercicioDao {
    @Query("SELECT * FROM exercicio WHERE idGrupo = :idGrupo") fun listaExercicios(idGrupo: Long): List<Exercicio>
    @Query("SELECT * FROM exercicio WHERE id = :id") fun localizaExercicio(id: Long): Exercicio
    @Insert fun salvaExercicio(exercicio: Exercicio)
    @Update fun alteraExercicio(exercicio: Exercicio)
    @Delete fun excluiExercicio(exercicio: Exercicio)
}
