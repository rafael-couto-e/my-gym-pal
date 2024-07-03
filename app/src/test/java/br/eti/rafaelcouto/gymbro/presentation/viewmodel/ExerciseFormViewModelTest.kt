package br.eti.rafaelcouto.gymbro.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import br.eti.rafaelcouto.gymbro.domain.usecase.ExerciseUseCaseAbs
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ExerciseFormViewModelTest {

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
}
