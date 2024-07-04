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
        assertThat(sut.uiState.value.workoutName).isEmpty()
        assertThat(sut.uiState.value.isButtonEnabled).isFalse()

        sut.uiState.value.onWorkoutNameChanged("Workout")

        assertThat(sut.uiState.value.workoutName).isEqualTo("Workout")
        assertThat(sut.uiState.value.isButtonEnabled).isTrue()

        sut.uiState.value.onWorkoutNameChanged("")

        assertThat(sut.uiState.value.workoutName).isEmpty()
        assertThat(sut.uiState.value.isButtonEnabled).isFalse()
    }

    @Test
    fun loadContentNoIdTest() = runTest {
        every { mockSavedStateHandle.get<Long>(any()) }.returns(null)
        assertThat(sut.uiState.value.workoutId).isEqualTo(0)
        assertThat(sut.uiState.value.workoutName).isEmpty()
        assertThat(sut.uiState.value.isButtonEnabled).isFalse()
        assertThat(sut.uiState.value.successMessage).isEqualTo(R.string.workout_created)

        sut.loadContent()

        verify { mockSavedStateHandle.get<Long>("workoutId") }
        coVerify(inverse = true) { mockUseCase.getWorkoutById(any()) }
        assertThat(sut.uiState.value.workoutId).isEqualTo(0)
        assertThat(sut.uiState.value.workoutName).isEmpty()
        assertThat(sut.uiState.value.isButtonEnabled).isFalse()
        assertThat(sut.uiState.value.successMessage).isEqualTo(R.string.workout_created)
    }

    @Test
    fun loadContentNoWorkoutTest() = runTest {
        every { mockSavedStateHandle.get<Long>(any()) }.returns(1L)
        coEvery { mockUseCase.getWorkoutById(any()) }.returns(null)
        assertThat(sut.uiState.value.workoutId).isEqualTo(0)
        assertThat(sut.uiState.value.workoutName).isEmpty()
        assertThat(sut.uiState.value.isButtonEnabled).isFalse()
        assertThat(sut.uiState.value.successMessage).isEqualTo(R.string.workout_created)

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
        assertThat(sut.uiState.value.workoutId).isEqualTo(0)
        assertThat(sut.uiState.value.workoutName).isEmpty()
        assertThat(sut.uiState.value.isButtonEnabled).isFalse()
        assertThat(sut.uiState.value.successMessage).isEqualTo(R.string.workout_created)

        sut.loadContent()

        verify { mockSavedStateHandle.get<Long>("workoutId") }
        coVerify { mockUseCase.getWorkoutById(1) }
        assertThat(sut.uiState.value.workoutId).isEqualTo(expected.id)
        assertThat(sut.uiState.value.workoutName).isEqualTo(expected.name)
        assertThat(sut.uiState.value.isButtonEnabled).isTrue()
        assertThat(sut.uiState.value.successMessage).isEqualTo(R.string.workout_updated)
    }

    @Test
    fun saveExistingWorkoutTest() = runTest {
        val expected = TestUtils.generateWorkout(id = 1)
        val input = TestUtils.generateWorkout(id = 1, name = "test")
        every { mockSavedStateHandle.get<Long>(any()) }.returns(1L)
        coEvery { mockUseCase.getWorkoutById(any()) }.returns(expected)
        sut.loadContent()
        assertThat(sut.uiState.value.workoutId).isEqualTo(1)
        assertThat(sut.uiState.value.workoutName).isEqualTo("Workout 1")

        sut.uiState.value.onWorkoutNameChanged("test")
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
