package br.eti.rafaelcouto.gymbro.presentation.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CheckboxTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun checkboxNotCheckedAndEnabledTest() {
        composeTestRule.run {
            var isChecked = false
            setContent {
                Checkbox(
                    modifier = Modifier.semantics { testTag = "checkbox" },
                    checked = isChecked,
                    enabled = true,
                    onCheckedChange = { isChecked = it }
                )
            }

            onNodeWithTag("checkbox").assertIsEnabled()
            onNodeWithTag("checkbox").assertIsOff()

            assertThat(isChecked).isFalse()
            onNodeWithTag("checkbox").performClick()
            assertThat(isChecked).isTrue()
        }
    }

    @Test
    fun checkboxCheckedAndEnabledTest() {
        composeTestRule.run {
            var isChecked = true
            setContent {
                Checkbox(
                    modifier = Modifier.semantics { testTag = "checkbox" },
                    checked = isChecked,
                    enabled = true,
                    onCheckedChange = { isChecked = it }
                )
            }

            onNodeWithTag("checkbox").assertIsEnabled()
            onNodeWithTag("checkbox").assertIsOn()

            assertThat(isChecked).isTrue()
            onNodeWithTag("checkbox").performClick()
            assertThat(isChecked).isFalse()
        }
    }

    @Test
    fun checkboxNotCheckedAndDisabledTest() {
        composeTestRule.run {
            var isChecked = false
            setContent {
                Checkbox(
                    modifier = Modifier.semantics { testTag = "checkbox" },
                    checked = isChecked,
                    enabled = false,
                    onCheckedChange = { isChecked = it }
                )
            }

            onNodeWithTag("checkbox").assertIsNotEnabled()
            onNodeWithTag("checkbox").assertIsOff()

            assertThat(isChecked).isFalse()
            onNodeWithTag("checkbox").performClick()
            assertThat(isChecked).isFalse()
        }
    }

    @Test
    fun checkboxCheckedAndDisabledTest() {
        composeTestRule.run {
            var isChecked = true
            setContent {
                Checkbox(
                    modifier = Modifier.semantics { testTag = "checkbox" },
                    checked = isChecked,
                    enabled = false,
                    onCheckedChange = { isChecked = it }
                )
            }

            onNodeWithTag("checkbox").assertIsNotEnabled()
            onNodeWithTag("checkbox").assertIsOn()

            assertThat(isChecked).isTrue()
            onNodeWithTag("checkbox").performClick()
            assertThat(isChecked).isTrue()
        }
    }
}
