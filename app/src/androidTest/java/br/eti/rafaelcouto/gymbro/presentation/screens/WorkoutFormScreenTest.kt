package br.eti.rafaelcouto.gymbro.presentation.screens

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.eti.rafaelcouto.gymbro.R
import br.eti.rafaelcouto.gymbro.TestUtils
import br.eti.rafaelcouto.gymbro.presentation.GymBroApp
import br.eti.rafaelcouto.gymbro.presentation.uistate.MainActivityUiState
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WorkoutFormScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun workoutFormScreenMainStateNoWorkoutTest() {
        composeTestRule.run {
            setContent {
                val (state, setState) = remember { mutableStateOf(MainActivityUiState()) }

                GymBroApp(
                    topAppBarTitle = stringResource(id = state.titleRes),
                    showsBackButton = state.showsBackButton,
                    floatingActionButton = state.floatingActionButton,
                    topAppBarActions = state.topAppBarActions
                ) {
                    WorkoutFormScreen(
                        setMainActivityState = setState
                    )
                }
            }

            onNodeWithText("Cadastrar treino").assertIsDisplayed()
            onNodeWithText("Editar treino").assertDoesNotExist()
            onNodeWithTag("backButton").assertIsDisplayed()
        }
    }

    @Test
    fun workoutFormScreenMainStateEditWorkoutTest() {
        composeTestRule.run {
            val workout = TestUtils.generateWorkout(id = 1)

            setContent {
                val (state, setState) = remember { mutableStateOf(MainActivityUiState()) }

                GymBroApp(
                    topAppBarTitle = stringResource(id = state.titleRes),
                    showsBackButton = state.showsBackButton,
                    floatingActionButton = state.floatingActionButton,
                    topAppBarActions = state.topAppBarActions
                ) {
                    WorkoutFormScreen(
                        setMainActivityState = setState,
                        state = TestUtils.generateWorkoutFormScreenUiState(
                            workout = workout
                        )
                    )
                }
            }

            onNodeWithText("Cadastrar treino").assertDoesNotExist()
            onNodeWithText("Editar treino").assertIsDisplayed()
            onNodeWithTag("backButton").assertIsDisplayed()
        }
    }

    @Test
    fun workoutFormScreenNoWorkoutTest() {
        composeTestRule.run {
            setContent {
                GymBroApp {
                    WorkoutFormScreen()
                }
            }

            onNodeWithTag("workoutNameField").run {
                assertIsDisplayed()
                assert(hasText(""))
            }

            onNodeWithText("Salvar treino").run {
                assertIsDisplayed()
                assertIsNotEnabled()
            }
        }
    }

    @Test
    fun workoutFormScreenEditWorkoutTest() {
        composeTestRule.run {
            val workout = TestUtils.generateWorkout(id = 1)

            setContent {
                GymBroApp {
                    WorkoutFormScreen(
                        state = TestUtils.generateWorkoutFormScreenUiState(
                            workout = workout
                        )
                    )
                }
            }

            onNodeWithTag("workoutNameField").run {
                assertIsDisplayed()
                assert(hasText(workout.name))
            }

            onNodeWithText("Salvar treino").run {
                assertIsDisplayed()
                assertIsNotEnabled()
            }
        }
    }

    @Test
    fun workoutFormScreenNoWorkoutButtonEnabledTest() {
        composeTestRule.run {
            var workoutSaved = false
            var successMessage: String? = null

            setContent {
                GymBroApp {
                    WorkoutFormScreen(
                        onSaveWorkout = { workoutSaved = true },
                        showMessage = { successMessage = it },
                        state = TestUtils.generateWorkoutFormScreenUiState(
                            isButtonEnabled = true
                        )
                    )
                }
            }

            onNodeWithText("Salvar treino").run {
                assertIsDisplayed()
                assertIsEnabled()

                assertThat(workoutSaved).isFalse()
                assertThat(successMessage).isNull()
                performClick()
                assertThat(workoutSaved).isTrue()
                assertThat(successMessage).isEqualTo("Treino criado!")
            }
        }
    }

    @Test
    fun workoutFormScreenEditWorkoutButtonEnabledTest() {
        composeTestRule.run {
            val workout = TestUtils.generateWorkout(id = 1)

            var workoutSaved = false
            var successMessage: String? = null

            setContent {
                GymBroApp {
                    WorkoutFormScreen(
                        onSaveWorkout = { workoutSaved = true },
                        showMessage = { successMessage = it },
                        state = TestUtils.generateWorkoutFormScreenUiState(
                            workout = workout,
                            isButtonEnabled = true,
                            successMessage = R.string.workout_updated
                        )
                    )
                }
            }

            onNodeWithText("Salvar treino").run {
                assertIsDisplayed()
                assertIsEnabled()

                assertThat(workoutSaved).isFalse()
                assertThat(successMessage).isNull()
                performClick()
                assertThat(workoutSaved).isTrue()
                assertThat(successMessage).isEqualTo("Treino atualizado!")
            }
        }
    }
}
