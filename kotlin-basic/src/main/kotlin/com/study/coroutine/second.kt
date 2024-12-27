package com.study.com.study.corutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class Second {

}

fun main() = runBlocking {
    co1()
    co2()
}

suspend fun co1() {
    delay(1000)
    println("1초뒤 동작")
}

suspend fun co2() {
    delay(500)
    println("0.5 초뒤 동장")
}
