package br.eti.rafaelcouto.mygympal.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "",
    var isLast: Boolean = false
)
