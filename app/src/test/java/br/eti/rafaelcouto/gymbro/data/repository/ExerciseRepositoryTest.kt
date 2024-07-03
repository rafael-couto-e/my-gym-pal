package br.eti.rafaelcouto.gymbro.data.repository

import br.eti.rafaelcouto.gymbro.TestUtils
import br.eti.rafaelcouto.gymbro.data.db.dao.ExerciseDao
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ExerciseRepositoryTest {

    private lateinit var sut: ExerciseRepositoryAbs

    @MockK(relaxUnitFun = true)
    private lateinit var mockDao: ExerciseDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = ExerciseRepository(mockDao)
    }

    @Test
    fun getAllExercisesTest() = runTest {
        val expected = listOf(
            TestUtils.generateExercise(id = 1, workoutId = 1),
            TestUtils.generateExercise(id = 2, workoutId = 1),
            TestUtils.generateExercise(id = 3, workoutId = 1)
        )
        every { mockDao.getAllExercises(any()) }.returns(flowOf(expected))

        val actual = sut.getAllExercises(1).last()

        assertThat(actual).isEqualTo(expected)
        verify { mockDao.getAllExercises(1) }
    }

    @Test
    fun findExerciseByIdTest() = runTest {
        val expected = TestUtils.generateExercise(id = 1, workoutId = 1)
        coEvery { mockDao.findExerciseById(any()) }.returns(expected)

        val actual = sut.findExerciseById(1)

        assertThat(actual).isEqualTo(expected)
        coVerify { mockDao.findExerciseById(1) }
    }

    @Test
    fun createExerciseTest() = runTest {
        val input = TestUtils.generateExercise(name = "Exercise", workoutId = 1)

        sut.createExercise(input)

        coVerify { mockDao.createExercise(input) }
    }

    @Test
    fun updateExerciseTest() = runTest {
        val input = TestUtils.generateExercise(id = 1, workoutId = 1)

        sut.updateExercise(input)

        coVerify { mockDao.updateExercise(input) }
    }

    @Test
    fun deleteExerciseTest() = runTest {
        sut.deleteExercise(1)

        coVerify { mockDao.deleteExercise(1) }
    }
}
