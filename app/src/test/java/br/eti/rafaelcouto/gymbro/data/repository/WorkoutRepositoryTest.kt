package br.eti.rafaelcouto.gymbro.data.repository

import br.eti.rafaelcouto.gymbro.TestUtils
import br.eti.rafaelcouto.gymbro.data.db.dao.WorkoutDao
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
class WorkoutRepositoryTest {

    private lateinit var sut: WorkoutRepositoryAbs

    @MockK(relaxUnitFun = true)
    private lateinit var mockDao: WorkoutDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = WorkoutRepository(mockDao)
    }

    @Test
    fun getAllWorkoutsTest() = runTest {
        val expected = listOf(
            TestUtils.generateWorkout(id = 1, isLast = true),
            TestUtils.generateWorkout(id = 2),
            TestUtils.generateWorkout(id = 3)
        )
        every { mockDao.getAllWorkouts() }.returns(flowOf(expected))

        val actual = sut.getAllWorkouts().last()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun findWorkoutByIdAsFlowTest() = runTest {
        val expected = TestUtils.generateWorkout(id = 1, isLast = true)
        every { mockDao.findWorkoutByIdAsFlow(any()) }.returns(flowOf(expected))

        val actual = sut.findWorkoutByIdAsFlow(1).last()

        assertThat(actual).isEqualTo(expected)
        verify { mockDao.findWorkoutByIdAsFlow(1) }
    }

    @Test
    fun findWorkoutByIdTest() = runTest {
        val expected = TestUtils.generateWorkout(id = 1, isLast = true)
        coEvery { mockDao.findWorkoutById(any()) }.returns(expected)

        val actual = sut.findWorkoutById(1)

        assertThat(actual).isEqualTo(expected)
        coVerify { mockDao.findWorkoutById(1) }
    }

    @Test
    fun createWorkoutTest() = runTest {
        val input = TestUtils.generateWorkout(name = "Workout", isLast = true)

        sut.createWorkout(input)

        coVerify { mockDao.createWorkout(input) }
    }

    @Test
    fun updateWorkoutTest() = runTest {
        val input = TestUtils.generateWorkout(id = 1, isLast = true)

        sut.updateWorkout(input)

        coVerify { mockDao.updateWorkout(input) }
    }

    @Test
    fun deleteWorkoutTest() = runTest {
        sut.deleteWorkout(1)

        coVerify { mockDao.deleteWorkout(1) }
    }
}
