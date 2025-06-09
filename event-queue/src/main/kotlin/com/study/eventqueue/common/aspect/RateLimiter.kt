package com.study.eventqueue.common.aspect

import com.study.eventqueue.config.logger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.context.expression.MethodBasedEvaluationContext
import org.springframework.core.DefaultParameterNameDiscoverer
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.TimeUnit

@Aspect
@Component
class RateLimiter(
    private val redisTemplate: RedisTemplate<String, String>,
) {

    companion object {
        val log = logger()
    }

    private val spelParser: ExpressionParser = SpelExpressionParser()

    // @Around("@annotation(sendRateLimiter)")
    fun applyRateLimiter(joinPoint: ProceedingJoinPoint): Any? {
        val methodSignature = joinPoint.signature as MethodSignature
        val method = methodSignature.method
        val sendRateLimiter = method.getAnnotation(SendRateLimiter::class.java)

        val key = generateKey(joinPoint, sendRateLimiter!!.key)
        val limitForPeriod = sendRateLimiter.count
        val limitRefreshPeriod = Duration.ofMillis(sendRateLimiter.time)

        val currentCount = getCount(key)

        if (isLimit(currentCount, limitForPeriod)) {
            log.warn("Rate limit exceeded for $key")
            throw IllegalArgumentException("${limitRefreshPeriod.toMinutes()}분간 최대 $limitForPeriod 가능")
        }

        incrementCount(key, limitRefreshPeriod)

        return joinPoint.proceed()
    }

    private fun generateKey(joinpoint: ProceedingJoinPoint, spEL: String): String {
        val parseKey = parseSpEL(joinpoint, spEL)
        return "cache:${parseKey.hashCode()}"

    }

    private fun parseSpEL(joinPoint: ProceedingJoinPoint, spEL: String): String {
        val methodSignature = joinPoint.signature as MethodSignature
        val method = methodSignature.method
        val defaultParameterNameDiscoverer = DefaultParameterNameDiscoverer()
        val context =
            MethodBasedEvaluationContext(joinPoint.target, method, joinPoint.args, defaultParameterNameDiscoverer)

        return spelParser.parseExpression(spEL).getValue(context, String::class.java)
            ?: throw IllegalArgumentException("spel expression could not be parsed")

    }

    private fun getCount(key: String): Long = redisTemplate.opsForValue().get(key)?.toLong() ?: 0L

    private fun isLimit(currentCount: Long, limitForPeriod: Int): Boolean = currentCount >= limitForPeriod

    private fun incrementCount(key: String, limitRefreshPeriod: Duration) {
        val currentCount = redisTemplate.opsForValue().increment(key)

        // 첫 카운트일 경우에는 만료시간을 설정
        if (currentCount == 1L) {
            redisTemplate.expire(key, limitRefreshPeriod.toMinutes(), TimeUnit.MINUTES)
        }
    }
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class SendRateLimiter(
    val key: String,
    val count: Int,
    val time: Long,
)