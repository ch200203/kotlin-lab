package com.study.eventqueue.controller

import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.Instant


/**
 * WebFlux 기반의 이벤트 큐 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/event-queue")
class EventQueueController(
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>
) {

    @PostMapping("/register")
    fun register(@RequestParam(name = "user_id") userId: Long): Mono<ResponseEntity<String>> {
        val unixTimestamp = Instant.now().epochSecond.toDouble()
        return reactiveRedisTemplate.opsForZSet()
            .add("user-queue", userId.toString(), unixTimestamp)
            .map { success ->
                if (success) ResponseEntity.ok("등록 완료")
                else ResponseEntity.badRequest().body("등록 실패")
            }
    }

    /**
     * 주기적으로 대기열 순위를 업데이트 해주기 위한 SSE
     */
    @GetMapping("/stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun streamQueue(@RequestParam userId: Long): Flux<ServerSentEvent<String>> {
        return Flux.interval(Duration.ofSeconds(1))
            .flatMap {
                reactiveRedisTemplate.opsForZSet()
                    .rank("user-queue", userId.toString()) // 현재 순위 조회
                    .map { rank ->
                        ServerSentEvent.builder("당신의 대기열 순위는 ${rank + 1}번입니다.")
                            .event("queue-update")
                            .build()
                    }
            }
    }

}
