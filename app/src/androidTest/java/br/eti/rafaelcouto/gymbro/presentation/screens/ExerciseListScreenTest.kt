package br.eti.rafaelcouto.gymbro.presentation.screens

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.eti.rafaelcouto.gymbro.TestUtils
import br.eti.rafaelcouto.gymbro.presentation.GymBroApp
import br.eti.rafaelcouto.gymbro.presentation.uistate.MainActivityUiState
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExerciseListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun exerciseListScreenEmptyMainStateTest() {
        composeTestRule.run {
            var fabClicked = false
            var onMenuToggle = false

            setContent {
                val (state, setState) = remember { mutableStateOf(MainActivityUiState()) }

                GymBroApp(
                    topAppBarTitle = state.title.orEmpty(),
                    showsBackButton = state.showsBackButton,
                    floatingActionButton = state.floatingActionButton,
                    topAppBarActions = state.topAppBarActions
                ) {
                    ExerciseListScreen(
                        onAddExercise = { fabClicked = true },
                        onMenuToggle = { onMenuToggle = it },
                        setMainActivityState = setState,
                        state = TestUtils.generateExerciseListUiState()
                    )
                }
            }

            onNodeWithText("Workout 0").assertIsDisplayed()
            onNodeWithTag("backButton").assertIsDisplayed()

            onNodeWithTag("fab").run {
                assertIsDisplayed()

                assertThat(fabClicked).isFalse()
                performClick()
                assertThat(fabClicked).isTrue()
            }

            onNodeWithTag("finishWorkoutButton").assertDoesNotExist()

            onNodeWithTag("dropDownMenuButton").run {
                assertIsDisplayed()

                assertThat(onMenuToggle).isFalse()
                performClick()
                assertThat(onMenuToggle).isTrue()
            }

            onNodeWithTag("dropDownMenuContent").assertDoesNotExist()
            onNodeWithTag("editAction").assertDoesNotExist()
            onNodeWithTag("deleteAction").assertDoesNotExist()
        }
    }

    @Test
    fun exerciseListScreenExpandedMainStateTest() {
        composeTestRule.run {
            var editWorkout: Long? = null
            var deleteWorkoutClicked = false

            setContent {
                val (state, setState) = remember { mutableStateOf(MainActivityUiState()) }

                GymBroApp(
                    topAppBarTitle = state.title.orEmpty(),
                    showsBackButton = state.showsBackButton,
                    floatingActionButton = state.floatingActionButton,
                    topAppBarActions = state.topAppBarActions
                ) {
                    ExerciseListScreen(
                        onEditWorkoutClick = { editWorkout = it },
                        onDeleteWorkoutClicked = { deleteWorkoutClicked = true },
                        setMainActivityState = setState,
                        state = TestUtils.generateExerciseListUiState(
                            isMenuExpanded = true
                        )
                    )
                }
            }

            onNodeWithTag("dropDownMenuContent").assertIsDisplayed()

            onNodeWithTag("editAction").run {
                assertIsDisplayed()

                assertThat(editWorkout).isNull()
                performClick()
                assertThat(editWorkout).isEqualTo(0)
            }

            onNodeWithTag("deleteAction").run {
                assertIsDisplayed()

                assertThat(deleteWorkoutClicked).isFalse()
                performClick()
                assertThat(deleteWorkoutClicked).isTrue()
            }
        }
    }

    @Test
    fun exerciseListScreenFilledMainStateTest() {
        composeTestRule.run {
            var workoutFinished = false

            setContent {
                val (state, setState) = remember { mutableStateOf(MainActivityUiState()) }

                GymBroApp(
                    topAppBarTitle = state.title.orEmpty(),
                    showsBackButton = state.showsBackButton,
                    floatingActionButton = state.floatingActionButton,
                    topAppBarActions = state.topAppBarActions
                ) {
                    ExerciseListScreen(
                        onFinishWorkout = { workoutFinished = true },
                        setMainActivityState = setState,
                        state = TestUtils.generateExerciseListUiState(
                            exercises = listOf(
                                TestUtils.generateExerciseUi(id = 1),
                                TestUtils.generateExerciseUi(id = 2)
                            ),
                            isMenuExpanded = true
                        )
                    )
                }
            }

            onNodeWithTag("finishWorkoutButton").run {
                assertIsDisplayed()
                assertIsEnabled()

                assertThat(workoutFinished).isFalse()
                performClick()
                assertThat(workoutFinished).isTrue()
            }
        }
    }

    @Test
    fun exerciseListScreenFinishedMainStateTest() {
        composeTestRule.run {
            val exercises = listOf(
                TestUtils.generateExerciseUi(id = 1, setsState = listOf(true, true, true)),
                TestUtils.generateExerciseUi(id = 2, setsState = listOf(true, true, true))
            )

            setContent {
                val (state, setState) = remember { mutableStateOf(MainActivityUiState()) }

                GymBroApp(
                    topAppBarTitle = state.title.orEmpty(),
                    showsBackButton = state.showsBackButton,
                    floatingActionButton = state.floatingActionButton,
                    topAppBarActions = state.topAppBarActions
                ) {
                    ExerciseListScreen(
                        setMainActivityState = setState,
                        state = TestUtils.generateExerciseListUiState(
                            exercises = exercises,
                            canFinishWorkout = false
                        )
                    )
                }
            }

            onNodeWithTag("finishWorkoutButton").run {
                assertIsDisplayed()
                assertIsNotEnabled()
            }
        }
    }

    @Test
    fun exerciseListScreenEmptyTest() {
        composeTestRule.run {
            setContent {
                GymBroApp {
                    ExerciseListScreen(
                        state = TestUtils.generateExerciseListUiState()
                    )
                }
            }


            onNodeWithText("Não há exercícios cadastrados").assertIsDisplayed()
        }
    }

    @Test
    fun exerciseListScreenFilledTest() {
        composeTestRule.run {
            var editExerciseId = 0L
            var increaseLoadId = 0L
            var decreaseLoadId = 0L

            val exercises = listOf(
                TestUtils.generateExerciseUi(id = 1, load = 10),
                TestUtils.generateExerciseUi(id = 2, minReps = 10, maxReps = 10),
                TestUtils.generateExerciseUi(id = 3, sets = 4, load = 30)
            )

            setContent {
                GymBroApp {
                    ExerciseListScreen(
                        onEditExerciseClick = { editExerciseId = it.id },
                        onIncreaseLoad = { increaseLoadId = it.id },
                        onDecreaseLoad = { decreaseLoadId = it.id },
                        state = TestUtils.generateExerciseListUiState(
                            exercises = exercises
                        )
                    )
                }
            }

            onNodeWithText("Não há exercícios cadastrados").assertDoesNotExist()

            onNodeWithText("Exercise 1").assertIsDisplayed()
            onNodeWithText("3 x 8 - 12 repetições").assertIsDisplayed()
            onNodeWithText("10kg").assertIsDisplayed()

            onNodeWithText("Exercise 2").assertIsDisplayed()
            onNodeWithText("3 x 10 repetições").assertIsDisplayed()
            onNodeWithText("20kg").assertIsDisplayed()

            onNodeWithText("Exercise 3").assertIsDisplayed()
            onNodeWithText("4 x 8 - 12 repetições").assertIsDisplayed()
            onNodeWithText("30kg").assertIsDisplayed()

            onNodeWithTag("edit-1").run {
                assertIsDisplayed()

                assertThat(editExerciseId).isEqualTo(0)
                performClick()
                assertThat(editExerciseId).isEqualTo(1)
            }

            onNodeWithTag("increase-2").run {
                assertIsDisplayed()

                assertThat(increaseLoadId).isEqualTo(0)
                performClick()
                assertThat(increaseLoadId).isEqualTo(2)
            }

            onNodeWithTag("decrease-3").run {
                assertIsDisplayed()

                assertThat(decreaseLoadId).isEqualTo(0)
                performClick()
                assertThat(decreaseLoadId).isEqualTo(3)
            }
        }
    }

    @Test
    fun exerciseListScreenFilledSetsStateTest() {
        composeTestRule.run {
            val exercisesFinished = mutableListOf<String>()

            val exercises = listOf(
                TestUtils.generateExerciseUi(
                    id = 1,
                    load = 10,
                    sets = 2,
                    setsState = listOf(true, false)
                ),
                TestUtils.generateExerciseUi(
                    id = 2,
                    sets = 2,
                    load = 30,
                    setsState = listOf(true, true)
                )
            )

            setContent {
                GymBroApp {
                    ExerciseListScreen(
                        onSetFinshed = { exercise, set ->
                            exercisesFinished.add("${exercise.original.id}$set")
                        },
                        state = TestUtils.generateExerciseListUiState(
                            exercises = exercises
                        )
                    )
                }
            }

            onNodeWithTag("exercise-1-set-0").run {
                assertIsDisplayed()
                assertIsOn()
                assertIsEnabled()

                assertThat(exercisesFinished).doesNotContain("10")
                performClick()
                assertThat(exercisesFinished).contains("10")
            }

            onNodeWithTag("exercise-1-set-1").run {
                assertIsDisplayed()
                assertIsOff()
                assertIsEnabled()

                assertThat(exercisesFinished).doesNotContain("11")
                performClick()
                assertThat(exercisesFinished).contains("11")
            }

            onNodeWithTag("exercise-2-set-0").run {
                assertIsDisplayed()
                assertIsOn()
                assertIsNotEnabled()

                assertThat(exercisesFinished).doesNotContain("20")
                performClick()
                assertThat(exercisesFinished).doesNotContain("20")
            }

            onNodeWithTag("exercise-2-set-1").run {
                assertIsDisplayed()
                assertIsOn()
                assertIsNotEnabled()

                assertThat(exercisesFinished).doesNotContain("21")
                performClick()
                assertThat(exercisesFinished).doesNotContain("21")
            }
        }
    }
}
