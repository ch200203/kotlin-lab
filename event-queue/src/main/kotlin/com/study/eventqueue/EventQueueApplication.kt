package com.study.eventqueue

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping

@SpringBootApplication
class EventQueueApplication

fun main(args: Array<String>) {
    runApplication<EventQueueApplication>(*args)

    // 메인페이지 이동
    @GetMapping("/")
    fun index(): String {
        return "index";
    }
}
