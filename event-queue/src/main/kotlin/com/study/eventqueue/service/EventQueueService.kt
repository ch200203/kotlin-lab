package com.study.eventqueue.service

import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class EventQueueService(
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>
) {

    @Scheduled(fixedDelay = 2000)
    fun processQueue() {
        reactiveRedisTemplate.opsForZSet()
            .range("user-queue", 0, 0)
            .flatMap { userId ->
                println("입장 처리할 유저: $userId")

                // 유저 제거 + 입장 처리
                // TODO : redis 를 직접 조작하는게 아니라, Template 감싸는 객체를 개발
                reactiveRedisTemplate.opsForZSet().remove("user-queue", userId)
                    .thenReturn(userId)
            }
            .subscribe { userId ->
                // 실제로는 유저에게 알림을 보내거나 상태 플래그를 업데이트하는 방식으로 처리
                println("입장 완료: $userId")
            }
    }
}

