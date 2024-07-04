package br.eti.rafaelcouto.gymbro.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import br.eti.rafaelcouto.gymbro.CoroutinesTestRule
import br.eti.rafaelcouto.gymbro.TestUtils
import br.eti.rafaelcouto.gymbro.domain.model.Exercise
import br.eti.rafaelcouto.gymbro.domain.usecase.ExerciseUseCaseAbs
import br.eti.rafaelcouto.gymbro.domain.usecase.WorkoutUseCaseAbs
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ExerciseListViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @MockK
    private lateinit var mockSavedStateHandle: SavedStateHandle

    @MockK(relaxUnitFun = true)
    private lateinit var mockExerciseUseCase: ExerciseUseCaseAbs

    @MockK(relaxUnitFun = true)
    private lateinit var mockWorkoutUseCase: WorkoutUseCaseAbs

    private lateinit var sut: ExerciseListViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = ExerciseListViewModel(mockSavedStateHandle, mockExerciseUseCase, mockWorkoutUseCase)
    }

    @Test
    fun loadEmptyContentTest() = runTest {
        val expectedWorkout = TestUtils.generateWorkout(id = 1)
        every { mockSavedStateHandle.get<Long>(any()) }.returns(1L)
        every { mockExerciseUseCase.getAllExercises(any()) }.returns(flowOf(emptyList()))
        every { mockWorkoutUseCase.getWorkoutByIdAsFlow(any()) }.returns(flowOf(expectedWorkout))
        with (sut.uiState.value) {
            assertThat(exercises).isEmpty()
            assertThat(workout.id).isEqualTo(0)
            assertThat(canFinishWorkout).isTrue()
            assertThat(isMenuExpanded).isFalse()
            assertThat(shouldDisplayEmptyMessage).isTrue()
        }

        sut.loadContent()

        with (sut.uiState.value) {
            assertThat(exercises).isEmpty()
            assertThat(workout).isEqualTo(expectedWorkout)
            assertThat(canFinishWorkout).isTrue()
            assertThat(isMenuExpanded).isFalse()
            assertThat(shouldDisplayEmptyMessage).isTrue()
        }
        verify { mockExerciseUseCase.getAllExercises(1) }
        verify { mockWorkoutUseCase.getWorkoutByIdAsFlow(1) }
    }

    @Test
    fun loadContentTest() = runTest {
        val expectedExercises = listOf(
            TestUtils.generateExerciseUi(id = 1, workoutId = 1),
            TestUtils.generateExerciseUi(id = 2, workoutId = 1),
            TestUtils.generateExerciseUi(id = 3, workoutId = 1)
        )
        val expectedWorkout = TestUtils.generateWorkout(id = 1)
        every { mockSavedStateHandle.get<Long>(any()) }.returns(1L)
        every { mockExerciseUseCase.getAllExercises(any()) }.returns(flowOf(expectedExercises))
        every { mockWorkoutUseCase.getWorkoutByIdAsFlow(any()) }.returns(flowOf(expectedWorkout))
        with (sut.uiState.value) {
            assertThat(exercises).isEmpty()
            assertThat(workout.id).isEqualTo(0)
            assertThat(canFinishWorkout).isTrue()
            assertThat(isMenuExpanded).isFalse()
            assertThat(shouldDisplayEmptyMessage).isTrue()
        }

        sut.loadContent()

        with (sut.uiState.value) {
            assertThat(exercises).isEqualTo(expectedExercises)
            assertThat(workout).isEqualTo(expectedWorkout)
            assertThat(canFinishWorkout).isTrue()
            assertThat(isMenuExpanded).isFalse()
            assertThat(shouldDisplayEmptyMessage).isFalse()
        }
        verify { mockExerciseUseCase.getAllExercises(1) }
        verify { mockWorkoutUseCase.getWorkoutByIdAsFlow(1) }
    }

    @Test
    fun increaseLoadTest() = runTest {
        val input = TestUtils.generateExercise(id = 1)

        sut.increaseLoad(input)

        coVerify { mockExerciseUseCase.increaseLoad(input) }
    }

    @Test
    fun decreaseLoadTest() = runTest {
        val input = TestUtils.generateExercise(id = 1)

        sut.decreaseLoad(input)

        coVerify { mockExerciseUseCase.decreaseLoad(input) }
    }

    @Test
    fun finishSetTest() = runTest {
        val expectedExercises = listOf(
            TestUtils.generateExerciseUi(id = 1, workoutId = 1),
            TestUtils.generateExerciseUi(id = 2, workoutId = 1)
        )
        every { mockSavedStateHandle.get<Long>(any()) }.returns(1)
        every { mockExerciseUseCase.getAllExercises(any()) }.returns(flowOf(expectedExercises))
        every { mockWorkoutUseCase.getWorkoutByIdAsFlow(any()) }.returns(flowOf(TestUtils.generateWorkout(id = 1)))
        sut.loadContent()

        with (sut.uiState.value) {
            assertThat(exercises).hasSize(2)
            assertThat(exercises[0].setsState).isEqualTo(listOf(false, false, false))
            assertThat(exercises[1].setsState).isEqualTo(listOf(false, false, false))
            assertThat(canFinishWorkout).isTrue()
        }

        sut.finishSet(expectedExercises[0], 0)
        sut.finishSet(expectedExercises[1], 1)

        with (sut.uiState.value) {
            assertThat(exercises[0].setsState).isEqualTo(listOf(true, false, false))
            assertThat(exercises[1].setsState).isEqualTo(listOf(false, true, false))
            assertThat(canFinishWorkout).isTrue()
        }

        coVerify(inverse = true) { mockWorkoutUseCase.finishWorkout(any()) }
    }

    @Test
    fun finishAllSetsTest() = runTest {
        val expectedExercises = listOf(
            TestUtils.generateExerciseUi(id = 1, workoutId = 1),
            TestUtils.generateExerciseUi(id = 2, workoutId = 1)
        )
        every { mockSavedStateHandle.get<Long>(any()) }.returns(1)
        every { mockExerciseUseCase.getAllExercises(any()) }.returns(flowOf(expectedExercises))
        every { mockWorkoutUseCase.getWorkoutByIdAsFlow(any()) }.returns(flowOf(TestUtils.generateWorkout(id = 1)))
        sut.loadContent()

        with (sut.uiState.value) {
            assertThat(exercises).hasSize(2)
            assertThat(exercises[0].setsState).isEqualTo(listOf(false, false, false))
            assertThat(exercises[1].setsState).isEqualTo(listOf(false, false, false))
            assertThat(canFinishWorkout).isTrue()
        }

        sut.finishSet(expectedExercises[0], 0)
        sut.finishSet(expectedExercises[0], 1)
        sut.finishSet(expectedExercises[0], 2)
        sut.finishSet(expectedExercises[1], 0)
        sut.finishSet(expectedExercises[1], 1)
        sut.finishSet(expectedExercises[1], 2)

        with (sut.uiState.value) {
            assertThat(exercises[0].setsState).isEqualTo(listOf(true, true, true))
            assertThat(exercises[1].setsState).isEqualTo(listOf(true, true, true))
            assertThat(canFinishWorkout).isFalse()
        }
        coVerify { mockWorkoutUseCase.finishWorkout(1) }
    }

    @Test
    fun deleteWorkoutTest() = runTest {
        every { mockSavedStateHandle.get<Long>(any()) }.returns(1)
        every { mockExerciseUseCase.getAllExercises(any()) }.returns(flowOf(emptyList()))
        every { mockWorkoutUseCase.getWorkoutByIdAsFlow(any()) }.returns(flowOf(TestUtils.generateWorkout(id = 1)))
        sut.loadContent()

        sut.deleteWorkout()

        coVerify { mockWorkoutUseCase.deleteWorkout(1) }
    }

    @Test
    fun finishWorkoutTest() = runTest {
        val expectedExercises = listOf(
            TestUtils.generateExerciseUi(id = 1, workoutId = 1),
            TestUtils.generateExerciseUi(id = 2, workoutId = 1)
        )
        every { mockSavedStateHandle.get<Long>(any()) }.returns(1)
        every { mockExerciseUseCase.getAllExercises(any()) }.returns(flowOf(expectedExercises))
        every { mockWorkoutUseCase.getWorkoutByIdAsFlow(any()) }.returns(flowOf(TestUtils.generateWorkout(id = 1)))
        sut.loadContent()

        with (sut.uiState.value) {
            assertThat(exercises).hasSize(2)
            assertThat(exercises[0].setsState).isEqualTo(listOf(false, false, false))
            assertThat(exercises[1].setsState).isEqualTo(listOf(false, false, false))
            assertThat(canFinishWorkout).isTrue()
        }

        sut.finishWorkout()

        with (sut.uiState.value) {
            assertThat(exercises[0].setsState).isEqualTo(listOf(true, true, true))
            assertThat(exercises[1].setsState).isEqualTo(listOf(true, true, true))
            assertThat(canFinishWorkout).isFalse()
        }
        coVerify { mockWorkoutUseCase.finishWorkout(1) }
    }

    @Test
    fun testSetMenuState() = runTest {
        assertThat(sut.uiState.value.isMenuExpanded).isFalse()

        sut.setMenuState(true)
        assertThat(sut.uiState.value.isMenuExpanded).isTrue()

        sut.setMenuState(true)
        assertThat(sut.uiState.value.isMenuExpanded).isTrue()

        sut.setMenuState(false)
        assertThat(sut.uiState.value.isMenuExpanded).isFalse()
    }

    @Test
    fun updateExercisesAndKeepSetsStateTest() = runTest {
        val expectedExercises = listOf(
            TestUtils.generateExerciseUi(id = 1, workoutId = 1),
            TestUtils.generateExerciseUi(id = 2, workoutId = 1),
            TestUtils.generateExerciseUi(id = 3, workoutId = 1)
        )
        val exercisesFlow = MutableSharedFlow<List<Exercise.UI>>()
        every { mockSavedStateHandle.get<Long>(any()) }.returns(1)
        every { mockExerciseUseCase.getAllExercises(any()) }.returns(exercisesFlow)
        every { mockWorkoutUseCase.getWorkoutByIdAsFlow(any()) }.returns(flowOf(TestUtils.generateWorkout(id = 1)))
        sut.loadContent()
        exercisesFlow.emit(expectedExercises)
        sut.finishSet(expectedExercises[0], 0)
        sut.finishSet(expectedExercises[1], 1)
        sut.finishSet(expectedExercises[2], 1)
        sut.finishSet(expectedExercises[2], 2)

        with (sut.uiState.value) {
            assertThat(exercises[0].original.load).isEqualTo(20)
            assertThat(exercises[0].setsState).isEqualTo(listOf(true, false, false))
            assertThat(exercises[1].original.sets).isEqualTo(3)
            assertThat(exercises[1].setsState).isEqualTo(listOf(false, true, false))
            assertThat(exercises[2].original.sets).isEqualTo(3)
            assertThat(exercises[2].setsState).isEqualTo(listOf(false, true, true))
        }

        val updatedExercises = listOf(
            TestUtils.generateExerciseUi(id = 1, load = 21, workoutId = 1),
            TestUtils.generateExerciseUi(id = 2, sets = 4, workoutId = 1),
            TestUtils.generateExerciseUi(id = 3, sets = 2, workoutId = 1)
        )
        exercisesFlow.emit(updatedExercises)

        with (sut.uiState.value) {
            assertThat(exercises[0].original.load).isEqualTo(21)
            assertThat(exercises[0].setsState).isEqualTo(listOf(true, false, false))
            assertThat(exercises[1].original.sets).isEqualTo(4)
            assertThat(exercises[1].setsState).isEqualTo(listOf(false, true, false, false))
            assertThat(exercises[2].original.sets).isEqualTo(2)
            assertThat(exercises[2].setsState).isEqualTo(listOf(false, true))
        }
    }
}
