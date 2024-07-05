package br.eti.rafaelcouto.gymbro.presentation.components

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BottomButtonColumnTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bottomButtonColumnEnabledTest() {
        composeTestRule.run {
            var hasClicked = false

            setContent {
                BottomButtonColumn(
                    buttonText = "Save",
                    isButtonEnabled = true,
                    onButtonClick = { hasClicked = true }
                ) {
                    Text(text = "Element 1")
                }
            }

            onNodeWithText("Element 1").assertIsDisplayed()

            onNodeWithText("Save").run {
                assertIsDisplayed()
                assertIsEnabled()

                assertThat(hasClicked).isFalse()
                performClick()
                assertThat(hasClicked).isTrue()
            }
        }
    }

    @Test
    fun bottomButtonColumnDisabledTest() {
        composeTestRule.run {
            var hasClicked = false

            setContent {
                BottomButtonColumn(
                    buttonText = "Save",
                    isButtonEnabled = false,
                    onButtonClick = { hasClicked = true }
                ) {
                    Text(text = "Element 1")
                }
            }

            onNodeWithText("Element 1").assertIsDisplayed()

            onNodeWithText("Save").run {
                assertIsDisplayed()
                assertIsNotEnabled()

                assertThat(hasClicked).isFalse()
                performClick()
                assertThat(hasClicked).isFalse()
            }
        }
    }
}
