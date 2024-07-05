package br.eti.rafaelcouto.gymbro.presentation.components

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TextFieldTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun textFieldTest() {
        composeTestRule.run {
            setContent {
                val (value, setValue) = remember { mutableStateOf("") }

                TextField(
                    value = value,
                    label = "test",
                    onValueChange = setValue
                )
            }

            onNodeWithText("test").assertIsDisplayed()
            onNodeWithTag("textField").assertIsDisplayed()
            onNodeWithTag("textField").assert(hasText(""))

            onNodeWithTag("textField").performTextInput("gymbro")
            onNodeWithTag("textField").assert(hasText("gymbro"))
        }
    }
}
