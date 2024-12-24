package com.study.coboardexample.controller

import com.study.coboardexample.service.TestService
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeoutException


@RestController
@RequestMapping("/test")
class TestController(
    private val testService: TestService,
) {

    companion object {
        val logger = LoggerFactory.getLogger(TestController::class.java)
    }

    @Throws(InterruptedException::class, ExecutionException::class, TimeoutException::class)
    @GetMapping("/async")
    fun asyncTest(): String {
        for (i in 0..119) {
            logger.info("=============$i");
            MDC.put("a", i.toString())
            testService.asyncTest(i)
            logger.info("[${MDC.get("a")}]")
            logger.info("=============$i");
        }

        return "end"
    }

}
