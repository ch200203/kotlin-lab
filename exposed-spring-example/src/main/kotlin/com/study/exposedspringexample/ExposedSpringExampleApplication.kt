package com.study.exposedspringexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ExposedSpringExampleApplication

fun main(args: Array<String>) {
	runApplication<ExposedSpringExampleApplication>(*args)
}
