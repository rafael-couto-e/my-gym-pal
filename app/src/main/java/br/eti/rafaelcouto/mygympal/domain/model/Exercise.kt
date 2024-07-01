package br.eti.rafaelcouto.mygympal.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(
        entity = Workout::class,
        parentColumns = ["id"],
        childColumns = ["workoutId"],
        onDelete = ForeignKey.CASCADE
    )
])
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val sets: Byte,
    val minReps: Byte,
    val maxReps: Byte,
    val load: Short,
    @ColumnInfo(index = true) val workoutId: Long
) {
    data class UI(
        val original: Exercise,
        val setsState: List<Boolean> = MutableList(original.sets.toInt()) { false }.toList()
        // TODO check if there is a better way of doing this, maybe moving to UiState
    ) {

        val isSameNumberOfReps
            get() = original.minReps == original.maxReps

        val finished
            get() = setsState.all { it }
    }
}
