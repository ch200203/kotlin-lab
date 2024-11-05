package com.study.sseexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SseExampleApplication

fun main(args: Array<String>) {
    runApplication<SseExampleApplication>(*args)
}
