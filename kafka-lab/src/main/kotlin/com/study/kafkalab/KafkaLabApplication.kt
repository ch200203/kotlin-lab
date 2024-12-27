package com.study.kafkalab

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaLabApplication

fun main(args: Array<String>) {
    runApplication<KafkaLabApplication>(*args)
}
