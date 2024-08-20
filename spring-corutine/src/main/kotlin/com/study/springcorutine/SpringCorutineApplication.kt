package com.study.springcorutine

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class SpringCorutineApplication

fun main(args: Array<String>) {
    runApplication<SpringCorutineApplication>(*args)
}
