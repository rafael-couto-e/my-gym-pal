package br.eti.rafaelcouto.gymbro.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import br.eti.rafaelcouto.gymbro.CoroutinesTestRule
import br.eti.rafaelcouto.gymbro.domain.usecase.WorkoutUseCaseAbs
import br.eti.rafaelcouto.gymbro.R
import br.eti.rafaelcouto.gymbro.TestUtils
import br.eti.rafaelcouto.gymbro.data.structure.CircularLinkedList
import br.eti.rafaelcouto.gymbro.domain.model.Workout
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WorkoutFormViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @MockK
    private lateinit var mockSavedStateHandle: SavedStateHandle

    @MockK(relaxUnitFun = true)
    private lateinit var mockUseCase: WorkoutUseCaseAbs

    private lateinit var sut: WorkoutFormViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = WorkoutFormViewModel(mockSavedStateHandle, mockUseCase)
    }

    @Test
    fun onWorkoutNameChangedTest() = runTest {
        with (sut.uiState.value) {
            assertThat(workoutName).isEmpty()
            assertThat(isButtonEnabled).isFalse()

            onWorkoutNameChanged("Workout")
        }

        with (sut.uiState.value) {
            assertThat(workoutName).isEqualTo("Workout")
            assertThat(isButtonEnabled).isTrue()

            onWorkoutNameChanged("")
        }

        with (sut.uiState.value) {
            assertThat(workoutName).isEmpty()
            assertThat(isButtonEnabled).isFalse()
        }
    }

    @Test
    fun loadContentNoIdTest() = runTest {
        every { mockSavedStateHandle.get<Long>(any()) }.returns(null)
        with (sut.uiState.value) {
            assertThat(workoutId).isEqualTo(0)
            assertThat(workoutName).isEmpty()
            assertThat(isButtonEnabled).isFalse()
            assertThat(successMessage).isEqualTo(R.string.workout_created)
        }

        sut.loadContent()

        verify { mockSavedStateHandle.get<Long>("workoutId") }
        coVerify(inverse = true) { mockUseCase.getWorkoutById(any()) }
        with (sut.uiState.value) {
            assertThat(workoutId).isEqualTo(0)
            assertThat(workoutName).isEmpty()
            assertThat(isButtonEnabled).isFalse()
            assertThat(successMessage).isEqualTo(R.string.workout_created)
        }
    }

    @Test
    fun loadContentNoWorkoutTest() = runTest {
        every { mockSavedStateHandle.get<Long>(any()) }.returns(1L)
        coEvery { mockUseCase.getWorkoutById(any()) }.returns(null)
        with (sut.uiState.value) {
            assertThat(workoutId).isEqualTo(0)
            assertThat(workoutName).isEmpty()
            assertThat(isButtonEnabled).isFalse()
            assertThat(successMessage).isEqualTo(R.string.workout_created)
        }

        sut.loadContent()

        verify { mockSavedStateHandle.get<Long>("workoutId") }
        coVerify { mockUseCase.getWorkoutById(1) }
        assertThat(sut.uiState.value.workoutId).isEqualTo(0)
        assertThat(sut.uiState.value.workoutName).isEmpty()
        assertThat(sut.uiState.value.isButtonEnabled).isFalse()
        assertThat(sut.uiState.value.successMessage).isEqualTo(R.string.workout_created)
    }

    @Test
    fun loadContentEditWorkoutTest() = runTest {
        val expected = TestUtils.generateWorkout(id = 1)
        every { mockSavedStateHandle.get<Long>(any()) }.returns(1L)
        coEvery { mockUseCase.getWorkoutById(any()) }.returns(expected)
        with (sut.uiState.value) {
            assertThat(workoutId).isEqualTo(0)
            assertThat(workoutName).isEmpty()
            assertThat(isButtonEnabled).isFalse()
            assertThat(successMessage).isEqualTo(R.string.workout_created)
        }

        sut.loadContent()

        verify { mockSavedStateHandle.get<Long>("workoutId") }
        coVerify { mockUseCase.getWorkoutById(1) }
        with (sut.uiState.value) {
            assertThat(workoutId).isEqualTo(expected.id)
            assertThat(workoutName).isEqualTo(expected.name)
            assertThat(isButtonEnabled).isTrue()
            assertThat(successMessage).isEqualTo(R.string.workout_updated)
        }
    }

    @Test
    fun saveExistingWorkoutTest() = runTest {
        val expected = TestUtils.generateWorkout(id = 1)
        val input = TestUtils.generateWorkout(id = 1, name = "test")
        every { mockSavedStateHandle.get<Long>(any()) }.returns(1L)
        coEvery { mockUseCase.getWorkoutById(any()) }.returns(expected)
        sut.loadContent()

        with (sut.uiState.value) {
            assertThat(workoutId).isEqualTo(1)
            assertThat(workoutName).isEqualTo("Workout 1")

            onWorkoutNameChanged("test")
        }

        sut.saveWorkout()

        coVerify(inverse = true) { mockUseCase.createWorkout(any()) }
        coVerify { mockUseCase.updateWorkout(input) }
    }

    @Test
    fun saveNewWorkoutAsFirstTest() = runTest {
        val input = TestUtils.generateWorkout(name = "test", isLast = true)
        every { mockUseCase.getAllWorkouts() }.returns(flowOf(CircularLinkedList()))

        sut.uiState.value.onWorkoutNameChanged("test")

        sut.saveWorkout()
        verify { mockUseCase.getAllWorkouts() }
        coVerify { mockUseCase.createWorkout(input) }
        coVerify(inverse = true) { mockUseCase.updateWorkout(any()) }
    }

    @Test
    fun saveNewWorkoutAsNextTest() = runTest {
        val input = TestUtils.generateWorkout(name = "test")
        val expected = CircularLinkedList<Workout>().apply {
            addAll(
                listOf(
                    TestUtils.generateWorkout(id = 1, isLast = true),
                    TestUtils.generateWorkout(id = 2),
                    TestUtils.generateWorkout(id = 3)
                )
            )
        }
        every { mockUseCase.getAllWorkouts() }.returns(flowOf(expected))

        sut.uiState.value.onWorkoutNameChanged("test")

        sut.saveWorkout()
        verify { mockUseCase.getAllWorkouts() }
        coVerify { mockUseCase.createWorkout(input) }
        coVerify(inverse = true) { mockUseCase.updateWorkout(any()) }
    }
}
