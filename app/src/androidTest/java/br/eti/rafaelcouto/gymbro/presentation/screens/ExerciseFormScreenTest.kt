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
import br.eti.rafaelcouto.gymbro.presentation.uistate.ExerciseFormUiState
import br.eti.rafaelcouto.gymbro.presentation.uistate.MainActivityUiState
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExerciseFormScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun exerciseFormScreenMainStateNoExerciseTest() {
        composeTestRule.run {
            setContent {
                val (state, setState) = remember { mutableStateOf(MainActivityUiState()) }

                GymBroApp(
                    topAppBarTitle = stringResource(id = state.titleRes),
                    showsBackButton = state.showsBackButton,
                    floatingActionButton = state.floatingActionButton,
                    topAppBarActions = state.topAppBarActions
                ) {
                    ExerciseFormScreen(
                        setMainActivityState = setState
                    )
                }
            }

            onNodeWithText("Incluir exercício").assertIsDisplayed()
            onNodeWithText("Editar exercício").assertDoesNotExist()
            onNodeWithTag("backButton").assertIsDisplayed()
            onNodeWithTag("deleteAction").assertDoesNotExist()
        }
    }

    @Test
    fun exerciseFormScreenMainStateEditExerciseTest() {
        composeTestRule.run {
            val exercise = TestUtils.generateExercise(id = 1)
            var deleteClicked = false
            var deleteMessage: String? = null

            setContent {
                val (state, setState) = remember { mutableStateOf(MainActivityUiState()) }

                GymBroApp(
                    topAppBarTitle = stringResource(id = state.titleRes),
                    showsBackButton = state.showsBackButton,
                    floatingActionButton = state.floatingActionButton,
                    topAppBarActions = state.topAppBarActions
                ) {
                    ExerciseFormScreen(
                        setMainActivityState = setState,
                        onDeleteExerciseClicked = { deleteClicked = true },
                        showMessage = { deleteMessage = it },
                        state = TestUtils.generateExerciseFormUiState(
                            exercise = exercise
                        )
                    )
                }
            }

            onNodeWithText("Incluir exercício").assertDoesNotExist()
            onNodeWithText("Editar exercício").assertIsDisplayed()
            onNodeWithTag("backButton").assertIsDisplayed()
            onNodeWithTag("deleteAction").run {
                assertIsDisplayed()

                assertThat(deleteClicked).isFalse()
                assertThat(deleteMessage).isNull()
                performClick()
                assertThat(deleteClicked).isTrue()
                assertThat(deleteMessage).isEqualTo("Exercício excluído!")
            }
        }
    }

    @Test
    fun exerciseFormScreenNoExerciseTest() {
        composeTestRule.run {
            setContent {
               GymBroApp {
                    ExerciseFormScreen()
                }
            }

            onNodeWithTag("exerciseNameField").run {
                assertIsDisplayed()
                assert(hasText(""))
            }
            onNodeWithTag("numberOfSetsField").run {
                assertIsDisplayed()
                assert(hasText(""))
            }
            onNodeWithTag("minRepsField").run {
                assertIsDisplayed()
                assert(hasText(""))
            }
            onNodeWithTag("maxRepsField").run {
                assertIsDisplayed()
                assert(hasText(""))
            }
            onNodeWithTag("loadField").run {
                assertIsDisplayed()
                assert(hasText(""))
            }

            onNodeWithText("Salvar exercício").run {
                assertIsDisplayed()
                assertIsNotEnabled()
            }
        }
    }

    @Test
    fun exerciseFormScreenEditExerciseTest() {
        composeTestRule.run {
            val exercise = TestUtils.generateExercise(id = 1)

            setContent {
                GymBroApp {
                    ExerciseFormScreen(
                        state = TestUtils.generateExerciseFormUiState(
                            exercise = exercise
                        )
                    )
                }
            }

            onNodeWithTag("exerciseNameField").run {
                assertIsDisplayed()
                assert(hasText(exercise.name))
            }
            onNodeWithTag("numberOfSetsField").run {
                assertIsDisplayed()
                assert(hasText(exercise.sets.toString()))
            }
            onNodeWithTag("minRepsField").run {
                assertIsDisplayed()
                assert(hasText(exercise.minReps.toString()))
            }
            onNodeWithTag("maxRepsField").run {
                assertIsDisplayed()
                assert(hasText(exercise.maxReps.toString()))
            }
            onNodeWithTag("loadField").run {
                assertIsDisplayed()
                assert(hasText(exercise.load.toString()))
            }

            onNodeWithText("Salvar exercício").run {
                assertIsDisplayed()
                assertIsNotEnabled()
            }
        }
    }

    @Test
    fun exerciseFormScreenNoExerciseButtonEnabledTest() {
        composeTestRule.run {
            var hasSavedExercise = false
            var saveMessage: String? = null

            setContent {
                GymBroApp {
                    ExerciseFormScreen(
                        onSaveExercise = { hasSavedExercise = true },
                        showMessage = { saveMessage = it },
                        state = ExerciseFormUiState(
                            isButtonEnabled = true
                        )
                    )
                }
            }

            onNodeWithText("Salvar exercício").run {
                assertIsDisplayed()
                assertIsEnabled()

                assertThat(hasSavedExercise).isFalse()
                assertThat(saveMessage).isNull()

                performClick()

                assertThat(hasSavedExercise).isTrue()
                assertThat(saveMessage).isEqualTo("Exercício cadastrado!")
            }
        }
    }

    @Test
    fun exerciseFormScreenEditExerciseButtonEnabledTest() {
        composeTestRule.run {
            val exercise = TestUtils.generateExercise(id = 1)

            var hasSavedExercise = false
            var saveMessage: String? = null

            setContent {
                GymBroApp {
                    ExerciseFormScreen(
                        onSaveExercise = { hasSavedExercise = true },
                        showMessage = { saveMessage = it },
                        state = TestUtils.generateExerciseFormUiState(
                            exercise = exercise,
                            isButtonEnabled = true,
                            successMessage = R.string.exercise_updated
                        )
                    )
                }
            }

            onNodeWithText("Salvar exercício").run {
                assertIsDisplayed()
                assertIsEnabled()

                assertThat(hasSavedExercise).isFalse()
                assertThat(saveMessage).isNull()

                performClick()

                assertThat(hasSavedExercise).isTrue()
                assertThat(saveMessage).isEqualTo("Exercício atualizado!")
            }
        }
    }
}
