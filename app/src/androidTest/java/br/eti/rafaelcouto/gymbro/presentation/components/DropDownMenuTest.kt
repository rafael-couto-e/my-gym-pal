package br.eti.rafaelcouto.gymbro.presentation.components

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DropDownMenuTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun dropDownMenuCollapsedTest() {
        composeTestRule.run {
            var isExpanded = false
            setContent {
                DropDownMenu(
                    expanded = isExpanded,
                    onToggle = { isExpanded = it }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(text = "Test")
                        },
                        onClick = { /*TODO*/ }
                    )
                }
            }

            onNodeWithTag("dropDownMenuContent").assertDoesNotExist()
            onNodeWithText("Test").assertDoesNotExist()

            onNodeWithTag("dropDownMenuButton").run {
                assertIsDisplayed()

                assertThat(isExpanded).isFalse()
                performClick()
                assertThat(isExpanded).isTrue()
            }
        }
    }

    @Test
    fun dropDownMenuExpandedTest() {
        composeTestRule.run {
            var isExpanded = true
            setContent {
                DropDownMenu(
                    expanded = isExpanded,
                    onToggle = { isExpanded = it }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(text = "Test")
                        },
                        onClick = { /*TODO*/ }
                    )
                }
            }

            onNodeWithTag("dropDownMenuContent").assertIsDisplayed()
            onNodeWithText("Test").assertIsDisplayed()

            onNodeWithTag("dropDownMenuButton").run {
                assertIsDisplayed()

                assertThat(isExpanded).isTrue()
                performClick()
                assertThat(isExpanded).isFalse()
            }
        }
    }
}
