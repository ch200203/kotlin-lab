package com.study.coboardexample

import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class CompareTest {

    @Test
    fun test_hash_code_for_the_same_object() {
        val obj = TestObject(
            id = "123",
            date = LocalDate.now(),
            number = 42,
            list = listOf(1, 2, 3, 4, 5)
        )

        val obj2 = TestObject(
            id = "123",
            date = LocalDate.of(2025, 1, 2),
            number = 42,
            list = listOf(1, 2, 3, 4, 5)
            // list = listOf(5,1,3,4,2)
        )

        // When
        val hashCode1 = obj.getHashCode()
        val hashCode2 = obj2.getHashCode()

        println("hashCode1 : $hashCode1, hasCode2: $hashCode2")

        // Then
        assertEquals(hashCode1, hashCode2)
        assertEquals(obj.list.hashCode(), obj2.list.hashCode())
    }

    @Test
    fun change_list_hashcode() {
        val obj = TestObject(
            id = "123",
            date = LocalDate.now(),
            number = 42,
            list = listOf(1, 2, 3, 4, 5)
        )

        val obj2 = TestObject(
            id = "123",
            date = LocalDate.of(2025, 1, 2),
            number = 42,
            list = listOf(5, 1, 3, 4, 2),
        )

        // When
        val hashCode1 = obj.getHashCode()
        val hashCode2 = obj2.getHashCode()

        println("hashCode1 : $hashCode1, hasCode2: $hashCode2")

        // Then
        assertNotEquals(hashCode1, hashCode2)
        assertNotEquals(obj.list.hashCode(), obj2.list.hashCode())
    }
}


data class TestObject(
    val id: String,
    val date: LocalDate,
    val number: Int,
    val list: List<Int>
) {

    fun getHashCode() = hashCode();

    // override fun hashCode(): Int = Objects.hash(id, date, number, list.hashCode())
    override fun hashCode(): Int = Objects.hash(id, date, number, Objects.hash(list))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TestObject

        if (number != other.number) return false
        if (id != other.id) return false
        if (date != other.date) return false

        return true
    }

}