package com.study.coboardexample.service

import com.study.coboardexample.controller.TestController
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class TestService {

    companion object {
        val logger = LoggerFactory.getLogger(TestService::class.java)
    }

    @Async
    fun asyncTest(value: Int) {
        logger.info(MDC.get("a") + ":" + value)
    }
}
