package br.eti.rafaelcouto.gymbro.domain.usecase

import br.eti.rafaelcouto.gymbro.TestUtils
import br.eti.rafaelcouto.gymbro.data.repository.ExerciseRepositoryAbs
import br.eti.rafaelcouto.gymbro.domain.mapper.ExerciseMapperAbs
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
class ExerciseUseCaseTest {

    @MockK(relaxUnitFun = true)
    private lateinit var mockRepository: ExerciseRepositoryAbs

    @MockK
    private lateinit var mockMapper: ExerciseMapperAbs

    private lateinit var sut: ExerciseUseCaseAbs

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = ExerciseUseCase(mockRepository, mockMapper)
    }

    @Test
    fun getAllExercisesTest() = runTest {
        val mapperInput = listOf(
            TestUtils.generateExercise(id = 1, workoutId = 1),
            TestUtils.generateExercise(id = 2, workoutId = 1),
            TestUtils.generateExercise(id = 3, workoutId = 1)
        )

        val expected = listOf(
            TestUtils.generateExerciseUi(id = 1, workoutId = 1),
            TestUtils.generateExerciseUi(id = 2, workoutId = 1),
            TestUtils.generateExerciseUi(id = 3, workoutId = 1)
        )

        every { mockRepository.getAllExercises(any()) }.returns(flowOf(mapperInput))
        every { mockMapper.map(any()) }.returns(expected)

        val actual = sut.getAllExercises(1).last()

        assertThat(actual).isEqualTo(expected)

        coVerify { mockRepository.getAllExercises(1) }
        verify { mockMapper.map(mapperInput) }
    }

    @Test
    fun findExerciseByIdTest() = runTest {
        val expected = TestUtils.generateExercise(id = 1, workoutId = 1)
        coEvery { mockRepository.findExerciseById(any()) }.returns(expected)

        val actual = sut.findExerciseById(1)

        assertThat(actual).isEqualTo(expected)
        coVerify { mockRepository.findExerciseById(1) }
    }

    @Test
    fun createExerciseTest() = runTest {
        val input = TestUtils.generateExercise(id = 1, workoutId = 1)

        sut.createExercise(input)

        coVerify { mockRepository.createExercise(input) }
    }

    @Test
    fun updateExerciseTest() = runTest {
        val input = TestUtils.generateExercise(id = 1, workoutId = 1)

        sut.updateExercise(input)

        coVerify { mockRepository.updateExercise(input) }
    }

    @Test
    fun deleteExerciseTest() = runTest {
        sut.deleteExercise(1)

        coVerify { mockRepository.deleteExercise(1) }
    }

    @Test
    fun increaseLoadTest() = runTest {
        val input = TestUtils.generateExercise(id = 1, workoutId = 1)
        val expectedInput = TestUtils.generateExercise(id = 1, load = input.load.inc(), workoutId = 1)

        sut.increaseLoad(input)

        coVerify { mockRepository.updateExercise(expectedInput) }
    }

    @Test
    fun decreaseLoadTest() = runTest {
        val input = TestUtils.generateExercise(id = 1, workoutId = 1)
        val expectedInput = TestUtils.generateExercise(id = 1, load = input.load.dec(), workoutId = 1)

        sut.decreaseLoad(input)

        coVerify { mockRepository.updateExercise(expectedInput) }
    }
}
