package br.eti.rafaelcouto.gymbro.presentation.screens

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.eti.rafaelcouto.gymbro.R
import br.eti.rafaelcouto.gymbro.TestUtils
import br.eti.rafaelcouto.gymbro.data.structure.CircularLinkedList
import br.eti.rafaelcouto.gymbro.domain.model.Workout
import br.eti.rafaelcouto.gymbro.presentation.GymBroApp
import br.eti.rafaelcouto.gymbro.presentation.uistate.MainActivityUiState
import br.eti.rafaelcouto.gymbro.presentation.uistate.WorkoutListScrenUiState
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
            setContent {
                val (state, setState) = remember {
                    mutableStateOf(MainActivityUiState(titleRes = R.string.app_name))
                }

                GymBroApp(
                    topAppBarTitle = stringResource(id = state.titleRes),
                    showsBackButton = state.showsBackButton,
                    floatingActionButton = state.floatingActionButton,
                    topAppBarActions = state.topAppBarActions
                ) {
                    WorkoutListScreen(
                        setMainActivityState = setState
                    )
                }
            }

            onNodeWithText("Meus treinos").assertIsDisplayed()
            onNodeWithTag("backButton").assertDoesNotExist()
            onNodeWithTag("fab").assertIsDisplayed()
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

        composeTestRule.run {
            setContent {
                GymBroApp {
                    WorkoutListScreen(
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
        }
    }
}
