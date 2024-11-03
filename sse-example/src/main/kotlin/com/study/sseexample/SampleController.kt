package com.study.sseexample

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SampleController {

    @GetMapping(value = ["/sse/flow/connect"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    suspend fun sseExample(): Flow<String> = flow {

        for (i: Int in 1..10) {
            emit("Event at $i ${System.currentTimeMillis()}")
            delay(1000)  // 1초마다 이벤트 전송
        }

    }
}
