package com.study.eventqueue.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.CacheKeyPrefix
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.scheduling.annotation.EnableScheduling
import java.time.Duration

@Configuration
@EnableScheduling
class RedisConfig(
    @Value("\${spring.data.redis.host}") private val host: String,
    @Value("\${spring.data.redis.port}") private val port: Int,
) {
    @Bean(name = ["cacheManager"])
    fun cacheManager(connectionFactory: RedisConnectionFactory): RedisCacheManager {
        // 공통 캐시 설정 TTL 1시간, null 캐시 금지, prefix 적용
        val config = RedisCacheConfiguration.defaultCacheConfig()
            .disableCachingNullValues()
            .entryTtl(Duration.ofSeconds(3600))
            .computePrefixWith(CacheKeyPrefix.simple())
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))

        // 개별 캐시 설정 (이름별 TTL 다르게 설정)
        /**
         * Spring에서 @Cacheable 등을 사용할 때, value 속성에 해당 키 이름을 명시
         * ex) @Cacheable(value = ["CreateRedisKey"], key = "#id")
         */
        val cacheConfigs = mapOf<String, RedisCacheConfiguration>(
            "CreateRedisKey" to RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(600)),
            "SelectRedisKey" to RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(3600))
        )

        // RedisCacheManager 빌드 및 반환
        return RedisCacheManager.RedisCacheManagerBuilder
            .fromConnectionFactory(connectionFactory)
            .cacheDefaults(config)
            .withInitialCacheConfigurations(cacheConfigs).build()
    }

    // Lettuce 기반 연결 팩토리 생성
    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory = LettuceConnectionFactory(host, port)

    @Bean
    @Primary
    fun reactiveRedisConnectionFactory(): ReactiveRedisConnectionFactory = LettuceConnectionFactory(host, port)

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.keySerializer = StringRedisSerializer()
        // redisTemplate.valueSerializer = StringRedisSerializer()
        redisTemplate.connectionFactory = redisConnectionFactory()
        return redisTemplate
    }

    @Bean
    fun reactiveRedisTemplate(connectionFactory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, String> {
        val serializer: RedisSerializer<String> = StringRedisSerializer()
        val jackson2JsonRedisSerializer = Jackson2JsonRedisSerializer(String::class.java)

        val redisSerializationContext = RedisSerializationContext
            .newSerializationContext<String, String>()
            .key(serializer)
            .value(jackson2JsonRedisSerializer)
            .hashKey(serializer)
            .hashValue(jackson2JsonRedisSerializer)
            .build()

        return ReactiveRedisTemplate(connectionFactory, redisSerializationContext)
    }
}
