package br.eti.rafaelcouto.mygympal.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.eti.rafaelcouto.mygympal.domain.model.Grupo

@Dao
interface GrupoDao {
    @Query("SELECT * FROM grupo") fun listaGrupos(): List<Grupo>
    @Query("SELECT * FROM grupo WHERE grupo.id = :id") fun localizaGrupo(id: Long): Grupo
    @Insert fun salvaGrupo(grupo: Grupo)
    @Update fun alteraGrupo(grupo: Grupo)
    @Delete fun excluiGrupo(grupo: Grupo)
}
