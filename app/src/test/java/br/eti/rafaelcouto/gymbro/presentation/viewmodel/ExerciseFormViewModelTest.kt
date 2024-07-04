package br.eti.rafaelcouto.gymbro.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import br.eti.rafaelcouto.gymbro.CoroutinesTestRule
import br.eti.rafaelcouto.gymbro.R
import br.eti.rafaelcouto.gymbro.TestUtils
import br.eti.rafaelcouto.gymbro.domain.usecase.ExerciseUseCaseAbs
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ExerciseFormViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @MockK
    private lateinit var mockSavedStateHandle: SavedStateHandle

    @MockK(relaxUnitFun = true)
    private lateinit var mockExerciseUseCase: ExerciseUseCaseAbs

    private lateinit var sut: ExerciseFormViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = ExerciseFormViewModel(mockSavedStateHandle, mockExerciseUseCase)
    }

    @Test
    fun onValueChangeTest() = runTest {
        assertThat(sut.uiState.value.exerciseName).isEmpty()
        assertThat(sut.uiState.value.numberOfSets).isEmpty()
        assertThat(sut.uiState.value.minReps).isEmpty()
        assertThat(sut.uiState.value.maxReps).isEmpty()
        assertThat(sut.uiState.value.load).isEmpty()
        assertThat(sut.uiState.value.isButtonEnabled).isFalse()

        sut.uiState.value.onExerciseNameChange("test")
        assertThat(sut.uiState.value.isButtonEnabled).isFalse()
        sut.uiState.value.onNumberOfSetsChange("4")
        assertThat(sut.uiState.value.isButtonEnabled).isFalse()
        sut.uiState.value.onMinRepsChange("10")
        assertThat(sut.uiState.value.isButtonEnabled).isFalse()
        sut.uiState.value.onMaxRepsChange("10")
        assertThat(sut.uiState.value.isButtonEnabled).isFalse()
        sut.uiState.value.onLoadChange("50")
        assertThat(sut.uiState.value.isButtonEnabled).isTrue()

        assertThat(sut.uiState.value.exerciseName).isEqualTo("test")
        assertThat(sut.uiState.value.numberOfSets).isEqualTo("4")
        assertThat(sut.uiState.value.minReps).isEqualTo("10")
        assertThat(sut.uiState.value.maxReps).isEqualTo("10")
        assertThat(sut.uiState.value.load).isEqualTo("50")
    }

    @Test
    fun loadContentNoIdTest() = runTest {
        every { mockSavedStateHandle.get<Long>(any()) }.returns(0)

        assertThat(sut.uiState.value.exerciseId).isEqualTo(0)
        assertThat(sut.uiState.value.exerciseName).isEmpty()
        assertThat(sut.uiState.value.numberOfSets).isEmpty()
        assertThat(sut.uiState.value.minReps).isEmpty()
        assertThat(sut.uiState.value.maxReps).isEmpty()
        assertThat(sut.uiState.value.load).isEmpty()
        assertThat(sut.uiState.value.isButtonEnabled).isFalse()
        assertThat(sut.uiState.value.successMessage).isEqualTo(R.string.exercise_created)

        sut.loadContent()

        assertThat(sut.uiState.value.exerciseId).isEqualTo(0)
        assertThat(sut.uiState.value.exerciseName).isEmpty()
        assertThat(sut.uiState.value.numberOfSets).isEmpty()
        assertThat(sut.uiState.value.minReps).isEmpty()
        assertThat(sut.uiState.value.maxReps).isEmpty()
        assertThat(sut.uiState.value.load).isEmpty()
        assertThat(sut.uiState.value.isButtonEnabled).isFalse()
        assertThat(sut.uiState.value.successMessage).isEqualTo(R.string.exercise_created)

        verify { mockSavedStateHandle.get<Long>("exerciseId") }
        coVerify(inverse = true) { mockExerciseUseCase.findExerciseById(any()) }
    }

    @Test
    fun loadContentNonExistentExerciseTest() = runTest {
        every { mockSavedStateHandle.get<Long>(any()) }.returns(1)
        coEvery { mockExerciseUseCase.findExerciseById(any()) }.returns(null)

        assertThat(sut.uiState.value.exerciseId).isEqualTo(0)
        assertThat(sut.uiState.value.exerciseName).isEmpty()
        assertThat(sut.uiState.value.numberOfSets).isEmpty()
        assertThat(sut.uiState.value.minReps).isEmpty()
        assertThat(sut.uiState.value.maxReps).isEmpty()
        assertThat(sut.uiState.value.load).isEmpty()
        assertThat(sut.uiState.value.isButtonEnabled).isFalse()
        assertThat(sut.uiState.value.successMessage).isEqualTo(R.string.exercise_created)

        sut.loadContent()

        assertThat(sut.uiState.value.exerciseId).isEqualTo(0)
        assertThat(sut.uiState.value.exerciseName).isEmpty()
        assertThat(sut.uiState.value.numberOfSets).isEmpty()
        assertThat(sut.uiState.value.minReps).isEmpty()
        assertThat(sut.uiState.value.maxReps).isEmpty()
        assertThat(sut.uiState.value.load).isEmpty()
        assertThat(sut.uiState.value.isButtonEnabled).isFalse()
        assertThat(sut.uiState.value.successMessage).isEqualTo(R.string.exercise_created)

        verify { mockSavedStateHandle.get<Long>("exerciseId") }
        coVerify { mockExerciseUseCase.findExerciseById(1) }
    }

    @Test
    fun loadContentTest() = runTest {
        val expected = TestUtils.generateExercise(id = 1)
        every { mockSavedStateHandle.get<Long>(any()) }.returns(1)
        coEvery { mockExerciseUseCase.findExerciseById(any()) }.returns(expected)

        assertThat(sut.uiState.value.exerciseId).isEqualTo(0)
        assertThat(sut.uiState.value.exerciseName).isEmpty()
        assertThat(sut.uiState.value.numberOfSets).isEmpty()
        assertThat(sut.uiState.value.minReps).isEmpty()
        assertThat(sut.uiState.value.maxReps).isEmpty()
        assertThat(sut.uiState.value.load).isEmpty()
        assertThat(sut.uiState.value.isButtonEnabled).isFalse()
        assertThat(sut.uiState.value.successMessage).isEqualTo(R.string.exercise_created)

        sut.loadContent()

        assertThat(sut.uiState.value.exerciseId).isEqualTo(expected.id)
        assertThat(sut.uiState.value.exerciseName).isEqualTo(expected.name)
        assertThat(sut.uiState.value.numberOfSets).isEqualTo(expected.sets.toString())
        assertThat(sut.uiState.value.minReps).isEqualTo(expected.minReps.toString())
        assertThat(sut.uiState.value.maxReps).isEqualTo(expected.maxReps.toString())
        assertThat(sut.uiState.value.load).isEqualTo(expected.load.toString())
        assertThat(sut.uiState.value.isButtonEnabled).isTrue()
        assertThat(sut.uiState.value.successMessage).isEqualTo(R.string.exercise_updated)

        verify { mockSavedStateHandle.get<Long>("exerciseId") }
        coVerify { mockExerciseUseCase.findExerciseById(1) }
    }

    @Test
    fun saveExistingExerciseTest() = runTest {
        every { mockSavedStateHandle.get<Long>(any()) }.returns(1)
        coEvery { mockExerciseUseCase.findExerciseById(any()) }.returns(TestUtils.generateExercise(id = 1))
        sut.loadContent()

        sut.uiState.value.onExerciseNameChange("test")

        sut.saveExercise()

        coVerify(inverse = true) { mockExerciseUseCase.createExercise(any()) }
        coVerify { mockExerciseUseCase.updateExercise(TestUtils.generateExercise(id = 1, name = "test", workoutId = 1)) }
        verify { mockSavedStateHandle.get<Long>("exerciseId") }
        verify { mockSavedStateHandle.get<Long>("workoutId") }
    }

    @Test
    fun saveNewExerciseTest() = runTest {
        val input = TestUtils.generateExercise(
            name = "test",
            sets = 4,
            minReps = 10,
            maxReps = 10,
            load = 50,
            workoutId = 1
        )
        sut.uiState.value.onExerciseNameChange(input.name)
        sut.uiState.value.onNumberOfSetsChange(input.sets.toString())
        sut.uiState.value.onMinRepsChange(input.minReps.toString())
        sut.uiState.value.onMaxRepsChange(input.maxReps.toString())
        sut.uiState.value.onLoadChange(input.load.toString())
        every { mockSavedStateHandle.get<Long>(any()) }.returns(1)

        sut.saveExercise()

        coVerify { mockExerciseUseCase.createExercise(input) }
        coVerify(inverse = true) { mockExerciseUseCase.updateExercise(any()) }
        verify { mockSavedStateHandle.get<Long>("workoutId") }
    }

    @Test
    fun deleteExerciseTest() = runTest {
        val expected = TestUtils.generateExercise(id = 1)
        every { mockSavedStateHandle.get<Long>(any()) }.returns(1)
        coEvery { mockExerciseUseCase.findExerciseById(any()) }.returns(expected)
        sut.loadContent()

        sut.deleteExercise()

        coVerify { mockExerciseUseCase.deleteExercise(1) }
    }
}
