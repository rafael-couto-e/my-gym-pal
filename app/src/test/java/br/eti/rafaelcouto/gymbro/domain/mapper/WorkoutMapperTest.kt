package br.eti.rafaelcouto.gymbro.domain.mapper

import br.eti.rafaelcouto.gymbro.TestUtils
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WorkoutMapperTest {

    private lateinit var sut: WorkoutMapperAbs

    @Before
    fun setUp() {
        sut = WorkoutMapper()
    }

    @Test
    fun mapTest() {
        val input = listOf(
            TestUtils.generateWorkout(id = 1),
            TestUtils.generateWorkout(id = 2),
            TestUtils.generateWorkout(id = 3)
        )

        val actual = sut.map(input)

        assertThat(actual.size).isEqualTo(input.size)
        assertThat(actual[0].value).isEqualTo(input[0])
        assertThat(actual[1].value).isEqualTo(input[1])
        assertThat(actual[2].value).isEqualTo(input[2])
    }
}
