package com.study.collections

data class Person(val name: String, val age: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Person

        if (name != other.name) return false
        if (age != other.age) return false
        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + age
        return result
    }
}

fun main() {
    val person1 = Person(name = "tony", age = 12)
    val person2 = Person(name = "tony", age = 12)

    println(person1 == person2)

    val (name, age) = person1

//    println("이름 = $name, 나이 = $age")
//
//    val set = hashSetOf(person1)
//    println(set.contains(person1))
//
//    person1.name = "strange"
//    println(set.contains(person1))
}