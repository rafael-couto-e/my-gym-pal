package br.eti.rafaelcouto.gymbro.extensions

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class StringExtensionsTest {

    @Test
    fun validNumberToIntTest() {
        assertThat("50".toIntOrZero()).isEqualTo(50)
    }

    @Test
    fun invalidNumberToIntTest() {
        assertThat("gymbro".toIntOrZero()).isEqualTo(0)
    }
}
