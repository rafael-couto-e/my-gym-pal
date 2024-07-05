package br.eti.rafaelcouto.gymbro.extensions

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.eti.rafaelcouto.gymbro.data.structure.CircularLinkedList
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LazyListScopeExtensionsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun itemsFromCircularLinkedListTest() {
        val input = CircularLinkedList<String>().apply {
            addAll(listOf("first", "second", "third"))
        }

        composeTestRule.run {
            setContent {
                LazyColumn {
                    items(input) {
                        Text(text = "element: $it")
                    }
                }
            }

            onNodeWithText("element: first").assertIsDisplayed()
            onNodeWithText("element: second").assertIsDisplayed()
            onNodeWithText("element: third").assertIsDisplayed()
        }
    }
}
