package com.study.com.study.corutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

suspend fun first(): String {
    delay(1000) // 비동기 작업을 시뮬레이션
    return "first 결과"
}

suspend fun second(input: String): String {
    delay(1000) // 비동기 작업을 시뮬레이션
    return "second 결과, 입력: $input"
}

suspend fun third(input: String): String {
    delay(1000) // 비동기 작업을 시뮬레이션
    return "third 결과, 입력: $input"
}

fun main() = runBlocking {
    // 순차적인 비동기 작업 실행
    val firstResult = first()
    val secondResult = second(firstResult)
    val thirdResult = third(secondResult)

    println("모든 작업 완료, 결과: $thirdResult")
}
