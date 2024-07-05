package br.eti.rafaelcouto.gymbro.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FloatingActionButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun floatingActionButtonTest() {
        composeTestRule.run {
            var hasClicked = false
            
            setContent {
                FloatingActionButton(
                    icon = Icons.Default.Add,
                    contentDescription = "test",
                    onClick = { hasClicked = true }
                )
            }

            onNodeWithContentDescription("test").run {
                assertIsDisplayed()

                assertThat(hasClicked).isFalse()
                performClick()
                assertThat(hasClicked).isTrue()
            }
        }
    }
}
