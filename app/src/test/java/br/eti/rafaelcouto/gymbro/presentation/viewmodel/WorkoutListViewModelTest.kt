package br.eti.rafaelcouto.gymbro.presentation.viewmodel

import br.eti.rafaelcouto.gymbro.CoroutinesTestRule
import br.eti.rafaelcouto.gymbro.TestUtils
import br.eti.rafaelcouto.gymbro.data.structure.CircularLinkedList
import br.eti.rafaelcouto.gymbro.domain.model.Workout
import br.eti.rafaelcouto.gymbro.domain.usecase.WorkoutUseCaseAbs
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WorkoutListViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @MockK(relaxUnitFun = true)
    private lateinit var mockUseCase: WorkoutUseCaseAbs

    private lateinit var sut: WorkoutListViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = WorkoutListViewModel(mockUseCase)
    }

    @Test
    fun loadContentTest() = runTest {
        val expectedWorkouts = listOf(
            TestUtils.generateWorkout(id = 1, isLast = true),
            TestUtils.generateWorkout(id = 2),
            TestUtils.generateWorkout(id = 3)
        )
        val expected = CircularLinkedList<Workout>().apply { addAll(expectedWorkouts) }

        every { mockUseCase.getAllWorkouts() }.returns(flowOf(expected))
        with (sut.uiState.value) {
            assertThat(workouts.isEmpty()).isTrue()
            assertThat(shouldDisplayEmptyMessage).isTrue()
        }

        sut.loadContent()

        with (sut.uiState.value) {
            assertThat(workouts.size).isEqualTo(3)
            assertThat(workouts[0].value).isEqualTo(expectedWorkouts[0])
            assertThat(workouts[1].value).isEqualTo(expectedWorkouts[1])
            assertThat(workouts[2].value).isEqualTo(expectedWorkouts[2])
            assertThat(shouldDisplayEmptyMessage).isFalse()
        }
    }
}
