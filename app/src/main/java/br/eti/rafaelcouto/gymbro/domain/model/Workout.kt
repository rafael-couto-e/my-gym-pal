package br.eti.rafaelcouto.gymbro.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "",
    val isLast: Boolean = false
)
