package com.study.sseexample

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@RestController
class SampleController {

    @GetMapping(value = ["/sse/flow/connect"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    suspend fun sseExample(): Flow<String> = flow {

        for (i: Int in 1..10) {
            emit("Event at $i ${System.currentTimeMillis()}")
            delay(1000)  // 1초마다 이벤트 전송
        }

    }

    @GetMapping("/sse")
    fun streamEvents(): SseEmitter {
        val emitter = SseEmitter()
        val executor = Executors.newSingleThreadScheduledExecutor()

        executor.scheduleAtFixedRate(object : Runnable {
            var count = 0

            override fun run() {
                try {
                    if (count < 10) {
                        emitter.send("Event at ${System.currentTimeMillis()}")
                        count++
                    } else {
                        emitter.send("finish")
                        emitter.complete()
                        executor.shutdown()
                    }
                } catch (e: IOException) {
                    emitter.completeWithError(e)
                    executor.shutdown()
                }
            }
        }, 0, 1, TimeUnit.SECONDS) // 1초마다 이벤트 전송

        emitter.onCompletion {
            executor.shutdown()
        }

        return emitter
    }

    @GetMapping("/")
    fun home(): ModelAndView {
        return ModelAndView("index") // "index"는 templates 디렉토리의 index.html 파일을 의미
    }
}
