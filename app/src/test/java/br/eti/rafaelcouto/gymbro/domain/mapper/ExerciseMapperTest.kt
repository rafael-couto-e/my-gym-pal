package br.eti.rafaelcouto.gymbro.domain.mapper

import br.eti.rafaelcouto.gymbro.TestUtils
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ExerciseMapperTest {

    private lateinit var sut: ExerciseMapperAbs

    @Before
    fun setUp() {
        sut = ExerciseMapper()
    }

    @Test
    fun mapTest() {
        val input = listOf(
            TestUtils.generateExercise(id = 1),
            TestUtils.generateExercise(id = 2),
            TestUtils.generateExercise(id = 3)
        )

        val actual = sut.map(input)

        assertThat(actual.size).isEqualTo(input.size)
        assertThat(actual[0].original).isEqualTo(input[0])
        assertThat(actual[1].original).isEqualTo(input[1])
        assertThat(actual[2].original).isEqualTo(input[2])
    }
}
