package br.eti.rafaelcouto.gymbro.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
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
class BottomButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bottomButtonEnabledTest() {
        var isClicked = false

        composeTestRule.run {
            setContent {
                Box(modifier = Modifier.fillMaxSize()) {
                    BottomButton(
                        text = "test",
                        onClick = {
                            isClicked = true
                        }
                    )
                }
            }

            onNodeWithText("test").run {
                assertIsEnabled()

                assertThat(isClicked).isFalse()
                performClick()
                assertThat(isClicked).isTrue()
            }
        }
    }

    @Test
    fun bottomButtonDisabledTest() {
        var isClicked = false

        composeTestRule.run {
            setContent {
                Box(modifier = Modifier.fillMaxSize()) {
                    BottomButton(
                        text = "test",
                        isEnabled = false,
                        onClick = {
                            isClicked = true
                        }
                    )
                }
            }

            onNodeWithText("test").run {
                assertIsNotEnabled()

                assertThat(isClicked).isFalse()
                performClick()
                assertThat(isClicked).isFalse()
            }
        }
    }
}
