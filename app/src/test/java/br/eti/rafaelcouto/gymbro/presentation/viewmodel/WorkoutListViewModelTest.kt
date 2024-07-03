package br.eti.rafaelcouto.gymbro.presentation.viewmodel

import br.eti.rafaelcouto.gymbro.domain.usecase.WorkoutUseCaseAbs
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WorkoutListViewModelTest {

    @MockK(relaxUnitFun = true)
    private lateinit var mockUseCase: WorkoutUseCaseAbs

    private lateinit var sut: WorkoutListViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = WorkoutListViewModel(mockUseCase)
    }
}
