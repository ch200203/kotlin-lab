package com.study.sseexample

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@RestController
class SseController(
    private val cacheManager: CacheManager,
    private val sseService: SseService
) {
    private val emitters = ConcurrentHashMap<String, SseEmitter>()

    @GetMapping("/generate")
    fun generateUrl(): String {
        val key = UUID.randomUUID().toString()
        cacheManager.put(key, "N")
        println("key : ${cacheManager.get(key).toString()}")
        return "http://localhost:8080/sse?key=$key" // QR 코드 주소 반환
    }

    @GetMapping("/sse/{key}")
    fun streamEvents(@PathVariable key: String): SseEmitter {
        val timeout: Long = 60 * 1000L
        val emitter = SseEmitter(timeout)

        val cacheValue = cacheManager.get(key)
        println("cacheValue = ${cacheValue.toString()}")
        if (cacheValue == null) {
            emitter.send("Key $key not found")
            emitter.complete()
            return emitter
        }

        emitter.onCompletion {
            // 연결 완료 후 추가 메시지 없음
        }

        emitter.onTimeout {
            emitter.completeWithError(RuntimeException("Connection timed out"))
            cacheManager.remove(key) // 타임아웃 시 캐시에서 키 제거
        }

        sseService.checkKeyValue(key, emitter)
        return emitter
    }

    @PostMapping("/update/{key}")
    fun updateKey(@PathVariable key: String): String {
        println(cacheManager.containsKey(key).toString())

        if (cacheManager.get(key) != null) {
            cacheManager.put(key, "Y")
            return "Key $key 변경 완료!"
        }

        return "해당하는 key 를 찾을 수 없습니다."
    }
}
