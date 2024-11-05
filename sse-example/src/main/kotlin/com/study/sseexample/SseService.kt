package com.study.sseexample

import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Service
class SseService(
    private val cacheManager: CacheManager,
) {
    /**
     * 긴단한 구현을 위해 newSingleThreadScheduledExecutor 를 사용했지만...
     * Observer 를 사용 한다면? 리소스 낭비도 적고 괜찮지 않으려나?
     */
    fun checkKeyValue(
        key: String,
        emitter: SseEmitter,
    ) {
        println("key : $key")
        println("checkKey : $cacheManager.get(key)")

        val executor = Executors.newSingleThreadScheduledExecutor()
        executor.scheduleAtFixedRate({
            val value = cacheManager.get(key)
            if (value == "Y") {
                try {
                    emitter.send("Key $key 변경완료!!")
                    emitter.complete()
                    cacheManager.remove(key) // 완료 후 캐시에서 키 제거
                    executor.shutdown()
                } catch (e: IOException) {
                    emitter.completeWithError(e)
                    executor.shutdown()
                }
            } else if (value == "N") {
                try {
                    emitter.send("Key $key Value : ${cacheManager.get(key)}")
                } catch (e: IOException) {
                    emitter.completeWithError(e)
                    executor.shutdown()
                }
            }
        }, 0, 1, TimeUnit.SECONDS)
    }
}
