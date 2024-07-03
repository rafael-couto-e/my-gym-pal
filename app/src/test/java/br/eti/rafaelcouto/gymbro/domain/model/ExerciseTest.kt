package br.eti.rafaelcouto.gymbro.domain.model

import br.eti.rafaelcouto.gymbro.TestUtils
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ExerciseTest {

    @Test
    fun exerciseUiInitialSetStateTest() {
        val sut = Exercise.UI(original = TestUtils.generateExercise())
        assertThat(sut.original.sets).isEqualTo(3)

        assertThat(sut.setsState).hasSize(3)
        assertThat(sut.setsState).doesNotContain(true)
    }

    @Test
    fun exerciseUiIsSameNumberOfRepsTest() {
        val repsRange = Exercise.UI(original = TestUtils.generateExercise())
        assertThat(repsRange.isSameNumberOfReps).isFalse()

        val sameReps = Exercise.UI(
            original = TestUtils.generateExercise(minReps = 10, maxReps = 10)
        )
        assertThat(sameReps.isSameNumberOfReps).isTrue()
    }

    @Test
    fun exerciseUiFinishedTest() {
        val notFinished = Exercise.UI(
            original = TestUtils.generateExercise(),
            setsState = listOf(false, true, true)
        )
        assertThat(notFinished.finished).isFalse()

        val finished = Exercise.UI(
            original = TestUtils.generateExercise(),
            setsState = listOf(true, true, true)
        )
        assertThat(finished.finished).isTrue()
    }
}
