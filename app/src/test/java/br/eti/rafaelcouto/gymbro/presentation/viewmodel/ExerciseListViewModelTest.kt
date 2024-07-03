package br.eti.rafaelcouto.gymbro.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import br.eti.rafaelcouto.gymbro.domain.usecase.ExerciseUseCaseAbs
import br.eti.rafaelcouto.gymbro.domain.usecase.WorkoutUseCaseAbs
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ExerciseListViewModelTest {

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
}
