package com.study.sseexample

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class CacheManager {
    private val cache = ConcurrentHashMap<String, String>()

    fun put(key: String, value: String) {
        cache[key] = value
    }

    fun get(key: String): String? {
        return cache[key]
    }

    fun remove(key: String) {
        cache.remove(key)
    }

    fun containsKey(key: String): Boolean {
        return cache.containsKey(key)
    }
}
