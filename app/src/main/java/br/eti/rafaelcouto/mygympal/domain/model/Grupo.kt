package br.eti.rafaelcouto.mygympal.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Grupo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    var ultimo: Boolean = false
)
