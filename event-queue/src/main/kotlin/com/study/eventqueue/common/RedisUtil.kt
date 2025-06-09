package com.study.eventqueue.common

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.study.eventqueue.config.logger
import org.springframework.data.redis.core.RedisTemplate

class RedisUtil(
    val redisTemplate: RedisTemplate<String, String>,
    val objectMapper: ObjectMapper = ObjectMapper()
) {

    companion object {
        val log = logger()
    }

    fun hasKey(key: String): Boolean = redisTemplate.hasKey(key)

    fun objToJson(obj: Any): String? {
        try {
            return objectMapper.writeValueAsString(obj)
        } catch (e: JsonProcessingException) {
            log.warn("object to json error. message : ${e.message} , value = :${obj}")
            return null
        }
    }

}