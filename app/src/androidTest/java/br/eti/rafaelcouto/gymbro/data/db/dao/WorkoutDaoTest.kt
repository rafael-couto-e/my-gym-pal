package br.eti.rafaelcouto.gymbro.data.db.dao

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.eti.rafaelcouto.gymbro.TestUtils
import br.eti.rafaelcouto.gymbro.data.db.GymBroDatabase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WorkoutDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: GymBroDatabase
    private lateinit var sut: WorkoutDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, GymBroDatabase::class.java).build()
        sut = database.workoutDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getAllWorkoutsAndCreateWorkoutTest() = runTest {
        assertThat(sut.getAllWorkouts().first()).isEmpty()

        val input = TestUtils.generateWorkout(name = "Workout")
        sut.createWorkout(input)

        val expected = listOf(TestUtils.generateWorkout(id = 1, name = "Workout"))
        val actual = sut.getAllWorkouts().first()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun findWorkoutByIdAsFlowTest() = runTest {
        assertThat(sut.getAllWorkouts().first()).isEmpty()
        sut.createWorkout(TestUtils.generateWorkout(name = "Workout"))

        val expected = TestUtils.generateWorkout(id = 1, name = "Workout")
        val actual = sut.findWorkoutByIdAsFlow(1).first()
        assertThat(actual).isEqualTo(expected)
        assertThat(sut.findWorkoutByIdAsFlow(2).first()).isNull()
    }

    @Test
    fun findWorkoutByIdTest() = runTest {
        assertThat(sut.getAllWorkouts().first()).isEmpty()
        sut.createWorkout(TestUtils.generateWorkout(name = "Workout"))

        val expected = TestUtils.generateWorkout(id = 1, name = "Workout")
        val actual = sut.findWorkoutById(1)
        assertThat(actual).isEqualTo(expected)
        assertThat(sut.findWorkoutById(2)).isNull()
    }

    @Test
    fun updateWorkoutTest() = runTest {
        assertThat(sut.getAllWorkouts().first()).isEmpty()
        sut.createWorkout(TestUtils.generateWorkout())

        val old = requireNotNull(sut.findWorkoutById(1))
        assertThat(old.name).isEqualTo("Workout 0")
        val new = old.copy(name = "Workout 1")
        sut.updateWorkout(new)
        val actual = requireNotNull(sut.findWorkoutById(1))
        assertThat(actual).isEqualTo(new)
    }

    @Test
    fun deleteWorkoutTest() = runTest {
        assertThat(sut.getAllWorkouts().first()).isEmpty()
        sut.createWorkout(TestUtils.generateWorkout())
        sut.createWorkout(TestUtils.generateWorkout(name = "Workout 1"))

        val current = requireNotNull(sut.findWorkoutById(2))
        assertThat(current.name).isEqualTo("Workout 1")
        assertThat(sut.getAllWorkouts().first()).hasSize(2)

        sut.deleteWorkout(2)
        assertThat(sut.getAllWorkouts().first()).hasSize(1)
        assertThat(sut.findWorkoutById(2)).isNull()
    }

    @Test
    fun workoutDeleteCascadeTest() = runTest {
        assertThat(sut.getAllWorkouts().first()).isEmpty()
        sut.createWorkout(TestUtils.generateWorkout())
        sut.createWorkout(TestUtils.generateWorkout(name = "Workout 1"))

        assertThat(sut.findWorkoutById(1)).isNotNull()
        assertThat(sut.findWorkoutById(2)).isNotNull()

        database.exerciseDao().apply {
            createExercise(TestUtils.generateExercise(workoutId = 1))
            createExercise(TestUtils.generateExercise(workoutId = 2))

            assertThat(getAllExercises(1).first()).hasSize(1)
            assertThat(getAllExercises(2).first()).hasSize(1)
        }

        sut.deleteWorkout(2)

        assertThat(sut.findWorkoutById(1)).isNotNull()
        assertThat(sut.findWorkoutById(2)).isNull()

        database.exerciseDao().apply {
            assertThat(getAllExercises(1).first()).hasSize(1)
            assertThat(getAllExercises(2).first()).isEmpty()
        }
    }
}
