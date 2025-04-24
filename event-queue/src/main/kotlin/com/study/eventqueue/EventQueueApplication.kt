package com.study.eventqueue

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EventQueueApplication

fun main(args: Array<String>) {
    runApplication<EventQueueApplication>(*args)
}
