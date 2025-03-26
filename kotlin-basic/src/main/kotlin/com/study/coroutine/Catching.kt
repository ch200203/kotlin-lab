package com.study.com.study.coroutine

class CatchingExample {

    fun tryCatch(): Any {
        val a = 10

        if (a < 0) throw IllegalArgumentException()

        return convert(a)
    }

    private fun convert(a: Int): Any {
        val b = a + 10

        try {
            return b
        } catch (e: Exception) {
            throw IllegalArgumentException(e)
        }
    }
}


data class ADTO(
    val name: String,
    val age: Int,
    val phone: String,
    val certType: String,
)

data class RDTO (
    val name: String,
    val age: Int,
    val phone: String,
    val type: String,
)