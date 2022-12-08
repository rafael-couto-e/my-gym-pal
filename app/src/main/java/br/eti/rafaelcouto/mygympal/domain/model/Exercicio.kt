package br.eti.rafaelcouto.mygympal.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(
        entity = Grupo::class,
        parentColumns = ["id"],
        childColumns = ["idGrupo"],
        onDelete = ForeignKey.CASCADE
    )
])
data class Exercicio(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val numSeries: Byte,
    val minRepeticoes: Byte,
    val maxRepeticoes: Byte,
    var carga: Short,
    @ColumnInfo(index = true) val idGrupo: Long
) {
    data class UI(val original: Exercicio, var concluido: Boolean = false)
}
