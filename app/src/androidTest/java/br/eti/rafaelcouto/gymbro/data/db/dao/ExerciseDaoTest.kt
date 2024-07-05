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
class ExerciseDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: GymBroDatabase
    private lateinit var sut: ExerciseDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, GymBroDatabase::class.java).build()
        sut = database.exerciseDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getAllExercisesAndCreateExerciseTest() = runTest {
        assertThat(sut.getAllExercises(1).first()).isEmpty()

        val input = TestUtils.generateExercise(name = "Exercise", workoutId = 1)
        database.workoutDao().createWorkout(TestUtils.generateWorkout())
        sut.createExercise(input)

        val expected = listOf(TestUtils.generateExercise(id = 1, name = "Exercise", workoutId = 1))
        val actual = sut.getAllExercises(1).first()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun findExerciseByIdTest() = runTest {
        assertThat(sut.getAllExercises(1).first()).isEmpty()
        database.workoutDao().createWorkout(TestUtils.generateWorkout())
        assertThat(sut.findExerciseById(1)).isNull()

        sut.createExercise(TestUtils.generateExercise(name = "Exercise", workoutId = 1))

        val expected = TestUtils.generateExercise(id = 1, name = "Exercise", workoutId = 1)
        val actual = sut.findExerciseById(1)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun updateExerciseTest() = runTest {
        database.workoutDao().createWorkout(TestUtils.generateWorkout())
        sut.createExercise(TestUtils.generateExercise(workoutId = 1))

        assertThat(sut.findExerciseById(1)).isEqualTo(
            TestUtils.generateExercise(id = 1, name = "Exercise 0", workoutId = 1)
        )

        val expected = TestUtils.generateExercise(id = 1, name = "Exercise 1", workoutId = 1)
        sut.updateExercise(expected)
        val actual = sut.findExerciseById(1)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun deleteExerciseTest() = runTest {
        database.workoutDao().createWorkout(TestUtils.generateWorkout())
        sut.createExercise(TestUtils.generateExercise(workoutId = 1))
        assertThat(sut.findExerciseById(1)).isNotNull()

        sut.deleteExercise(1)

        assertThat(sut.findExerciseById(1)).isNull()
    }
}
