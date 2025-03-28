package com.study.com.study.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main () = runBlocking {
    launch {
        delay(1000L)
        println("World!")
    }

    println("Hello, ")
}
