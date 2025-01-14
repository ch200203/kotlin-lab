package com.study.coboardexample.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.*

@RestController
@RequestMapping("/api")
class TestController {

    @PostMapping("/hash")
    fun getHashCodeTestApi(@RequestBody dto: TestObject): String {
        val obj1 = dto.getHashCode()
        val obj2 = dto.hashCode()
        return "obj1 = $obj1, obj2 = $obj2"
    }
}

data class TestObject(
    val id: String,
    val date: LocalDate,
    val number: Int,
) {

    override fun hashCode(): Int = Objects.hash(id, date, number)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TestObject

        if (number != other.number) return false
        if (id != other.id) return false
        if (date != other.date) return false

        return true
    }

    fun getHashCode() = hashCode();

}
