package br.eti.rafaelcouto.gymbro.presentation.screens

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.eti.rafaelcouto.gymbro.TestUtils
import br.eti.rafaelcouto.gymbro.data.structure.CircularLinkedList
import br.eti.rafaelcouto.gymbro.domain.model.Workout
import br.eti.rafaelcouto.gymbro.presentation.GymBroApp
import br.eti.rafaelcouto.gymbro.presentation.uistate.MainActivityUiState
import br.eti.rafaelcouto.gymbro.presentation.uistate.WorkoutListScrenUiState
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WorkoutListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun workoutListScreenMainStateTest() {
        composeTestRule.run {
            var fabClicked = false

            setContent {
                val (state, setState) = remember { mutableStateOf(MainActivityUiState()) }

                GymBroApp(
                    topAppBarTitle = stringResource(id = state.titleRes),
                    showsBackButton = state.showsBackButton,
                    floatingActionButton = state.floatingActionButton,
                    topAppBarActions = state.topAppBarActions
                ) {
                    WorkoutListScreen(
                        onFabClicked = { fabClicked = true },
                        setMainActivityState = setState
                    )
                }
            }

            onNodeWithText("Meus treinos").assertIsDisplayed()
            onNodeWithTag("backButton").assertDoesNotExist()
            onNodeWithTag("fab").run {
                assertIsDisplayed()

                assertThat(fabClicked).isFalse()
                performClick()
                assertThat(fabClicked).isTrue()
            }
        }
    }

    @Test
    fun workoutListScreenEmptyTest() {
        composeTestRule.run {
            setContent {
                GymBroApp {
                    WorkoutListScreen()
                }
            }

            onNodeWithText("Não há treinos cadastrados").assertIsDisplayed()
        }
    }

    @Test
    fun workoutListScreenFilledTest() {
        val workouts = CircularLinkedList<Workout>().apply {
            add(TestUtils.generateWorkout(id = 1, isLast = true))
            add(TestUtils.generateWorkout(id = 2))
            add(TestUtils.generateWorkout(id = 3))
        }
        var selectedWorkout: Workout? = null

        composeTestRule.run {
            setContent {
                GymBroApp {
                    WorkoutListScreen(
                        onWorkoutSelected = { selectedWorkout = it },
                        state = WorkoutListScrenUiState(
                            workouts = workouts
                        )
                    )
                }
            }

            onNodeWithText("Não há treinos cadastrados").assertDoesNotExist()
            onNodeWithTag("workout-1").assertIsDisplayed()
            onNodeWithTag("workout-2").assertIsDisplayed()
            onNodeWithTag("workout-3").assertIsDisplayed()

            assertThat(selectedWorkout).isNull()
            onNodeWithTag("workout-1").performClick()
            assertThat(selectedWorkout?.id).isEqualTo(1)
        }
    }
}
