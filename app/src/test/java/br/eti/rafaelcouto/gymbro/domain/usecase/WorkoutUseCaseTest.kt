package br.eti.rafaelcouto.gymbro.domain.usecase

import br.eti.rafaelcouto.gymbro.TestUtils
import br.eti.rafaelcouto.gymbro.data.repository.WorkoutRepositoryAbs
import br.eti.rafaelcouto.gymbro.data.structure.CircularLinkedList
import br.eti.rafaelcouto.gymbro.domain.mapper.WorkoutMapperAbs
import br.eti.rafaelcouto.gymbro.domain.model.Workout
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
class WorkoutUseCaseTest {

    @MockK(relaxUnitFun = true)
    private lateinit var mockRepository: WorkoutRepositoryAbs

    @MockK
    private lateinit var mockMapper: WorkoutMapperAbs

    private lateinit var sut: WorkoutUseCaseAbs

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = WorkoutUseCase(mockRepository, mockMapper)
    }

    @Test
    fun getAllWorkoutsTest() = runTest {
        val mapperInput = listOf(
            TestUtils.generateWorkout(id = 1, isLast = true),
            TestUtils.generateWorkout(id = 2),
            TestUtils.generateWorkout(id = 3)
        )
        val expected = CircularLinkedList<Workout>().apply { addAll(mapperInput) }
        every { mockRepository.getAllWorkouts() }.returns(flowOf(mapperInput))
        every { mockMapper.map(any()) }.returns(expected)

        val actual = sut.getAllWorkouts().last()

        assertThat(actual.size).isEqualTo(expected.size)
        assertThat(actual[0].value).isEqualTo(expected[0].value)
        assertThat(actual[1].value).isEqualTo(expected[1].value)
        assertThat(actual[2].value).isEqualTo(expected[2].value)

        verify { mockMapper.map(mapperInput) }
    }

    @Test
    fun getWorkoutByIdAsFlowTest() = runTest {
        val expected = TestUtils.generateWorkout(id = 1, isLast = true)
        every { mockRepository.findWorkoutByIdAsFlow(any()) }.returns(flowOf(expected))

        val actual = sut.getWorkoutByIdAsFlow(1).last()

        assertThat(actual).isEqualTo(expected)
        verify { mockRepository.findWorkoutByIdAsFlow(1) }
    }

    @Test
    fun getWorkoutByIdTest() = runTest {
        val expected = TestUtils.generateWorkout(id = 1, isLast = true)
        coEvery { mockRepository.findWorkoutById(any()) }.returns(expected)

        val actual = sut.getWorkoutById(1)

        assertThat(actual).isEqualTo(expected)
        coVerify { mockRepository.findWorkoutById(1) }
    }

    @Test
    fun createWorkoutTest() = runTest {
        val input = TestUtils.generateWorkout(id = 1, isLast = true)

        sut.createWorkout(input)

        coVerify { mockRepository.createWorkout(input) }
    }

    @Test
    fun updateWorkoutTest() = runTest {
        val input = TestUtils.generateWorkout(id = 1, isLast = true)

        sut.updateWorkout(input)

        coVerify { mockRepository.updateWorkout(input) }
    }

    @Test
    fun deleteNonLastWorkout() = runTest {
        val mapperInput = listOf(
            TestUtils.generateWorkout(id = 1, isLast = true),
            TestUtils.generateWorkout(id = 2),
            TestUtils.generateWorkout(id = 3)
        )
        val output = CircularLinkedList<Workout>().apply { addAll(mapperInput) }
        every { mockRepository.getAllWorkouts() }.returns(flowOf(mapperInput))
        every { mockMapper.map(any()) }.returns(output)

        sut.deleteWorkout(2)

        coVerify { mockRepository.deleteWorkout(2) }
        coVerify(inverse = true) { mockRepository.updateWorkout(any()) }
        verify { mockMapper.map(mapperInput) }
    }

    @Test
    fun deleteLastWorkout() = runTest {
        val mapperInput = listOf(
            TestUtils.generateWorkout(id = 1, isLast = true),
            TestUtils.generateWorkout(id = 2),
            TestUtils.generateWorkout(id = 3)
        )
        val updateInput = TestUtils.generateWorkout(id = 2, isLast = true)
        val output = CircularLinkedList<Workout>().apply { addAll(mapperInput) }
        every { mockRepository.getAllWorkouts() }.returns(flowOf(mapperInput))
        every { mockMapper.map(any()) }.returns(output)

        sut.deleteWorkout(1)

        coVerify { mockRepository.deleteWorkout(1) }
        coVerify { mockRepository.updateWorkout(updateInput) }
        verify { mockMapper.map(mapperInput) }
    }

    @Test
    fun deleteSingleWorkout() = runTest {
        val mapperInput = listOf(
            TestUtils.generateWorkout(id = 1, isLast = true)
        )
        val output = CircularLinkedList<Workout>().apply { addAll(mapperInput) }
        every { mockRepository.getAllWorkouts() }.returns(flowOf(mapperInput))
        every { mockMapper.map(any()) }.returns(output)

        sut.deleteWorkout(1)

        coVerify { mockRepository.deleteWorkout(1) }
        coVerify(inverse = true) { mockRepository.updateWorkout(any()) }
        verify { mockMapper.map(mapperInput) }
    }

    @Test
    fun finishNonExistentWorkoutTest() = runTest {
        val mapperInput = listOf(
            TestUtils.generateWorkout(id = 1, isLast = true),
            TestUtils.generateWorkout(id = 2),
            TestUtils.generateWorkout(id = 3)
        )
        val output = CircularLinkedList<Workout>().apply { addAll(mapperInput) }
        every { mockRepository.getAllWorkouts() }.returns(flowOf(mapperInput))
        every { mockMapper.map(any()) }.returns(output)

        sut.finishWorkout(4)

        coVerify(inverse = true) { mockRepository.updateWorkout(any()) }
    }

    @Test
    fun finishLastWorkoutTest() = runTest {
        val lastWorkout = TestUtils.generateWorkout(id = 1)
        val nextWorkout = TestUtils.generateWorkout(id = 2, isLast = true)
        val mapperInput = listOf(
            TestUtils.generateWorkout(id = 1, isLast = true),
            TestUtils.generateWorkout(id = 2),
            TestUtils.generateWorkout(id = 3)
        )
        val output = CircularLinkedList<Workout>().apply { addAll(mapperInput) }
        every { mockRepository.getAllWorkouts() }.returns(flowOf(mapperInput))
        every { mockMapper.map(any()) }.returns(output)

        sut.finishWorkout(1)

        coVerify { mockRepository.updateWorkout(lastWorkout) }
        coVerify { mockRepository.updateWorkout(nextWorkout) }
    }

    @Test
    fun finishNonLastWorkoutTest() = runTest {
        val lastWorkout = TestUtils.generateWorkout(id = 1)
        val nextWorkout = TestUtils.generateWorkout(id = 3, isLast = true)
        val mapperInput = listOf(
            TestUtils.generateWorkout(id = 1, isLast = true),
            TestUtils.generateWorkout(id = 2),
            TestUtils.generateWorkout(id = 3)
        )
        val output = CircularLinkedList<Workout>().apply { addAll(mapperInput) }
        every { mockRepository.getAllWorkouts() }.returns(flowOf(mapperInput))
        every { mockMapper.map(any()) }.returns(output)

        sut.finishWorkout(2)

        coVerify { mockRepository.updateWorkout(lastWorkout) }
        coVerify { mockRepository.updateWorkout(nextWorkout) }
    }

    @Test
    fun finishBeforeLastWorkoutTest() = runTest {
        val mapperInput = listOf(
            TestUtils.generateWorkout(id = 1, isLast = true),
            TestUtils.generateWorkout(id = 2),
            TestUtils.generateWorkout(id = 3)
        )
        val output = CircularLinkedList<Workout>().apply { addAll(mapperInput) }
        every { mockRepository.getAllWorkouts() }.returns(flowOf(mapperInput))
        every { mockMapper.map(any()) }.returns(output)

        sut.finishWorkout(3)

        coVerify(inverse = true) { mockRepository.updateWorkout(any()) }
    }

    @Test
    fun finishSingleWorkoutTest() = runTest {
        val finishedWorkout = TestUtils.generateWorkout(id = 1)
        val output = CircularLinkedList<Workout>().apply { add(finishedWorkout) }
        every { mockRepository.getAllWorkouts() }.returns(flowOf(listOf(finishedWorkout)))
        every { mockMapper.map(any()) }.returns(output)

        sut.finishWorkout(1)

        coVerify(inverse = true) { mockRepository.updateWorkout(any()) }
    }
}
