package br.eti.rafaelcouto.gymbro.data.structure

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CircularLinkedListTest {

    private lateinit var sut: CircularLinkedList<Int>

    @Before
    fun setUp() {
        sut = CircularLinkedList()
    }

    @Test
    fun addElementsToListTest() {
        assertThat(sut.size).isEqualTo(0)

        sut.add(1)

        assertThat(sut.size).isEqualTo(1)
        assertThat(sut[0].value).isEqualTo(1)
        assertThat(sut[0].next).isNull()

        sut.add(2)

        assertThat(sut.size).isEqualTo(2)
        assertThat(sut[0].value).isEqualTo(1)
        assertThat(sut[0].next).isEqualTo(sut[1])
        assertThat(sut[1].value).isEqualTo(2)
        assertThat(sut[1].next).isEqualTo(sut[0])

        sut.add(3)

        assertThat(sut.size).isEqualTo(3)
        assertThat(sut[0].value).isEqualTo(1)
        assertThat(sut[0].next).isEqualTo(sut[1])
        assertThat(sut[1].value).isEqualTo(2)
        assertThat(sut[1].next).isEqualTo(sut[2])
        assertThat(sut[2].value).isEqualTo(3)
        assertThat(sut[2].next).isEqualTo(sut[0])
    }

    @Test
    fun addCollectionToListTest() {
        assertThat(sut.size).isEqualTo(0)

        sut.addAll(listOf(1))

        assertThat(sut.size).isEqualTo(1)
        assertThat(sut[0].value).isEqualTo(1)
        assertThat(sut[0].next).isNull()

        sut.addAll(listOf(2, 3))

        assertThat(sut.size).isEqualTo(3)
        assertThat(sut[0].value).isEqualTo(1)
        assertThat(sut[0].next).isEqualTo(sut[1])
        assertThat(sut[1].value).isEqualTo(2)
        assertThat(sut[1].next).isEqualTo(sut[2])
        assertThat(sut[2].value).isEqualTo(3)
        assertThat(sut[2].next).isEqualTo(sut[0])
    }

    @Test
    fun addElementNegativeIndex() {
        sut.addAll(listOf(1, 2, 3))
        assertThat(sut.size).isEqualTo(3)

        sut.add(-20, 0)

        assertThat(sut.size).isEqualTo(4)
        assertThat(sut[0].value).isEqualTo(0)
        assertThat(sut[0].next).isEqualTo(sut[1])
        assertThat(sut[1].value).isEqualTo(1)
        assertThat(sut[1].next).isEqualTo(sut[2])
        assertThat(sut[2].value).isEqualTo(2)
        assertThat(sut[2].next).isEqualTo(sut[3])
        assertThat(sut[3].value).isEqualTo(3)
        assertThat(sut[3].next).isEqualTo(sut[0])
    }

    @Test
    fun addElementIndexGreaterThanSize() {
        sut.addAll(listOf(1, 2, 3))
        assertThat(sut.size).isEqualTo(3)

        sut.add(20, 4)

        assertThat(sut.size).isEqualTo(4)
        assertThat(sut[0].value).isEqualTo(1)
        assertThat(sut[0].next).isEqualTo(sut[1])
        assertThat(sut[1].value).isEqualTo(2)
        assertThat(sut[1].next).isEqualTo(sut[2])
        assertThat(sut[2].value).isEqualTo(3)
        assertThat(sut[2].next).isEqualTo(sut[3])
        assertThat(sut[3].value).isEqualTo(4)
        assertThat(sut[3].next).isEqualTo(sut[0])
    }

    @Test
    fun addElementAsFirst() {
        sut.addAll(listOf(1, 2, 3))
        assertThat(sut.size).isEqualTo(3)

        sut.add(0, 0)

        assertThat(sut.size).isEqualTo(4)
        assertThat(sut[0].value).isEqualTo(0)
        assertThat(sut[0].next).isEqualTo(sut[1])
        assertThat(sut[1].value).isEqualTo(1)
        assertThat(sut[1].next).isEqualTo(sut[2])
        assertThat(sut[2].value).isEqualTo(2)
        assertThat(sut[2].next).isEqualTo(sut[3])
        assertThat(sut[3].value).isEqualTo(3)
        assertThat(sut[3].next).isEqualTo(sut[0])
    }

    @Test
    fun addElementAsLast() {
        sut.addAll(listOf(1, 2, 3))
        assertThat(sut.size).isEqualTo(3)

        sut.add(3, 4)

        assertThat(sut.size).isEqualTo(4)
        assertThat(sut[0].value).isEqualTo(1)
        assertThat(sut[0].next).isEqualTo(sut[1])
        assertThat(sut[1].value).isEqualTo(2)
        assertThat(sut[1].next).isEqualTo(sut[2])
        assertThat(sut[2].value).isEqualTo(3)
        assertThat(sut[2].next).isEqualTo(sut[3])
        assertThat(sut[3].value).isEqualTo(4)
        assertThat(sut[3].next).isEqualTo(sut[0])
    }

    @Test
    fun addElementInMiddle() {
        sut.addAll(listOf(1, 2, 3))
        assertThat(sut.size).isEqualTo(3)

        sut.add(1, 4)

        assertThat(sut.size).isEqualTo(4)
        assertThat(sut[0].value).isEqualTo(1)
        assertThat(sut[0].next).isEqualTo(sut[1])
        assertThat(sut[1].value).isEqualTo(4)
        assertThat(sut[1].next).isEqualTo(sut[2])
        assertThat(sut[2].value).isEqualTo(2)
        assertThat(sut[2].next).isEqualTo(sut[3])
        assertThat(sut[3].value).isEqualTo(3)
        assertThat(sut[3].next).isEqualTo(sut[0])
    }

    @Test
    fun removeFirstElementTest() {
        sut.addAll(listOf(1, 2, 3, 4))
        assertThat(sut.size).isEqualTo(4)

        assertThat(sut.remove(element = 1)).isTrue()

        assertThat(sut.size).isEqualTo(3)
        assertThat(sut[0].value).isEqualTo(2)
        assertThat(sut[0].next).isEqualTo(sut[1])
        assertThat(sut[1].value).isEqualTo(3)
        assertThat(sut[1].next).isEqualTo(sut[2])
        assertThat(sut[2].value).isEqualTo(4)
        assertThat(sut[2].next).isEqualTo(sut[0])
    }

    @Test
    fun removeLastElementTest() {
        sut.addAll(listOf(1, 2, 3, 4))
        assertThat(sut.size).isEqualTo(4)

        assertThat(sut.remove(element = 4)).isTrue()

        assertThat(sut.size).isEqualTo(3)
        assertThat(sut[0].value).isEqualTo(1)
        assertThat(sut[0].next).isEqualTo(sut[1])
        assertThat(sut[1].value).isEqualTo(2)
        assertThat(sut[1].next).isEqualTo(sut[2])
        assertThat(sut[2].value).isEqualTo(3)
        assertThat(sut[2].next).isEqualTo(sut[0])
    }

    @Test
    fun removeMiddleElementTest() {
        sut.addAll(listOf(1, 2, 3, 4))
        assertThat(sut.size).isEqualTo(4)

        assertThat(sut.remove(element = 2)).isTrue()

        assertThat(sut.size).isEqualTo(3)
        assertThat(sut[0].value).isEqualTo(1)
        assertThat(sut[0].next).isEqualTo(sut[1])
        assertThat(sut[1].value).isEqualTo(3)
        assertThat(sut[1].next).isEqualTo(sut[2])
        assertThat(sut[2].value).isEqualTo(4)
        assertThat(sut[2].next).isEqualTo(sut[0])
    }

    @Test
    fun removeOneOfThreeElementsTest() {
        sut.addAll(listOf(1, 2, 3))
        assertThat(sut.size).isEqualTo(3)

        assertThat(sut.remove(element = 2)).isTrue()

        assertThat(sut.size).isEqualTo(2)
        assertThat(sut[0].value).isEqualTo(1)
        assertThat(sut[0].next).isEqualTo(sut[1])
        assertThat(sut[1].value).isEqualTo(3)
        assertThat(sut[1].next).isEqualTo(sut[0])
    }

    @Test
    fun removeOneOfTwoElementsTest() {
        sut.addAll(listOf(1, 2))
        assertThat(sut.size).isEqualTo(2)

        assertThat(sut.remove(element = 2)).isTrue()

        assertThat(sut.size).isEqualTo(1)
        assertThat(sut[0].value).isEqualTo(1)
        assertThat(sut[0].next).isNull()
    }

    @Test
    fun removeSingleElementTest() {
        sut.add(1)
        assertThat(sut.size).isEqualTo(1)

        assertThat(sut.remove(element = 1)).isTrue()

        assertThat(sut.size).isEqualTo(0)
    }

    @Test
    fun removeNonExistentElementTest() {
        sut.addAll(listOf(1, 2, 3))
        assertThat(sut.size).isEqualTo(3)

        assertThat(sut.remove(element = 4)).isFalse()

        assertThat(sut.size).isEqualTo(3)
    }

    @Test
    fun removeFirstIndexTest() {
        sut.addAll(listOf(1, 2, 3, 4))
        assertThat(sut.size).isEqualTo(4)

        assertThat(sut.remove(index = 0)).isTrue()

        assertThat(sut.size).isEqualTo(3)
        assertThat(sut[0].value).isEqualTo(2)
        assertThat(sut[0].next).isEqualTo(sut[1])
        assertThat(sut[1].value).isEqualTo(3)
        assertThat(sut[1].next).isEqualTo(sut[2])
        assertThat(sut[2].value).isEqualTo(4)
        assertThat(sut[2].next).isEqualTo(sut[0])
    }

    @Test
    fun removeLastIndexTest() {
        sut.addAll(listOf(1, 2, 3, 4))
        assertThat(sut.size).isEqualTo(4)

        assertThat(sut.remove(index = 3)).isTrue()

        assertThat(sut.size).isEqualTo(3)
        assertThat(sut[0].value).isEqualTo(1)
        assertThat(sut[0].next).isEqualTo(sut[1])
        assertThat(sut[1].value).isEqualTo(2)
        assertThat(sut[1].next).isEqualTo(sut[2])
        assertThat(sut[2].value).isEqualTo(3)
        assertThat(sut[2].next).isEqualTo(sut[0])
    }

    @Test
    fun removeMiddleIndexTest() {
        sut.addAll(listOf(1, 2, 3, 4))
        assertThat(sut.size).isEqualTo(4)

        assertThat(sut.remove(index = 1)).isTrue()

        assertThat(sut.size).isEqualTo(3)
        assertThat(sut[0].value).isEqualTo(1)
        assertThat(sut[0].next).isEqualTo(sut[1])
        assertThat(sut[1].value).isEqualTo(3)
        assertThat(sut[1].next).isEqualTo(sut[2])
        assertThat(sut[2].value).isEqualTo(4)
        assertThat(sut[2].next).isEqualTo(sut[0])
    }

    @Test
    fun removeOneOfThreeIndexesTest() {
        sut.addAll(listOf(1, 2, 3))
        assertThat(sut.size).isEqualTo(3)

        assertThat(sut.remove(index = 1)).isTrue()

        assertThat(sut.size).isEqualTo(2)
        assertThat(sut[0].value).isEqualTo(1)
        assertThat(sut[0].next).isEqualTo(sut[1])
        assertThat(sut[1].value).isEqualTo(3)
        assertThat(sut[1].next).isEqualTo(sut[0])
    }

    @Test
    fun removeOneOfTwoIndexesTest() {
        sut.addAll(listOf(1, 2))
        assertThat(sut.size).isEqualTo(2)

        assertThat(sut.remove(index = 1)).isTrue()

        assertThat(sut.size).isEqualTo(1)
        assertThat(sut[0].value).isEqualTo(1)
        assertThat(sut[0].next).isNull()
    }

    @Test
    fun removeSingleIndexTest() {
        sut.add(1)
        assertThat(sut.size).isEqualTo(1)

        assertThat(sut.remove(index = 0)).isTrue()

        assertThat(sut.size).isEqualTo(0)
    }

    @Test
    fun removeNegativeIndexTest() {
        sut.addAll(listOf(1, 2, 3))
        assertThat(sut.size).isEqualTo(3)

        assertThat(sut.remove(index = -20)).isFalse()

        assertThat(sut.size).isEqualTo(3)
    }

    @Test
    fun removeIndexGreaterThanSizeTest() {
        sut.addAll(listOf(1, 2, 3))
        assertThat(sut.size).isEqualTo(3)

        assertThat(sut.remove(index = 20)).isFalse()

        assertThat(sut.size).isEqualTo(3)
    }

    @Test
    fun firstWithExistingPredicateTest() {
        sut.addAll(listOf(1, 2, 3))

        assertThat(sut.first { it > 1 }.value).isEqualTo(2)
    }

    @Test(expected = NoSuchElementException::class)
    fun firstWithNonExistingPredicateTest() {
        sut.addAll(listOf(1, 2, 3))

        try {
            sut.first { it > 3 }
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(NoSuchElementException::class.java)
            throw e
        }
    }

    @Test
    fun firstOrNullWithExistingPredicateTest() {
        sut.addAll(listOf(1, 2, 3))

        assertThat(sut.firstOrNull { it > 1 }?.value).isEqualTo(2)
    }

    @Test
    fun firstOrNullWithNonExistingPredicateTest() {
        sut.addAll(listOf(1, 2, 3))

        assertThat(sut.firstOrNull { it > 3 }).isNull()
    }

    @Test
    fun filterTest() {
        sut.addAll(listOf(1, 2, 3))
        assertThat(sut.size).isEqualTo(3)

        assertThat(sut.filter { it >= 2 }.size).isEqualTo(2)
        assertThat(sut.filter { it > 3 }.isEmpty()).isTrue()
    }

    @Test
    fun isEmptyTest() {
        assertThat(sut.isEmpty()).isTrue()

        sut.addAll(listOf(1, 2, 3))

        assertThat(sut.isEmpty()).isFalse()
    }
}
