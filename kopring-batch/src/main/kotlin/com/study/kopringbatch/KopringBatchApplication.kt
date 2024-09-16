package com.study.kopringbatch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
// @EnableBatchProcessing, spring batch 3.0 이상에서는 이 어노테이션 사용이 어려움
class KopringBatchApplication

fun main(args: Array<String>) {
	runApplication<KopringBatchApplication>(*args)
}
