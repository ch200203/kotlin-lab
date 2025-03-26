package com.study.kafkalab.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import org.springframework.util.backoff.FixedBackOff


@EnableKafka
@Configuration
class KafkaConsumerConfig {

    @Value("\${kafka.bootstrap-servers}")
    private lateinit var bootstrapServer: String

    companion object {
        val logger = LoggerFactory.getLogger(KafkaConsumerConfig::class.java)
    }

    @Bean
    fun defaultKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Any> =
        ConcurrentKafkaListenerContainerFactory<String, Any>().apply {
            consumerFactory = consumerFactory("defaultGroup")
            setConcurrency(1)
            setAutoStartup(true)
        }

    private fun consumerFactory(groupId: String): ConsumerFactory<String, Any?> {
        val props = HashMap<String, Any>()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServer
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId

        val jsonDeserializer = JsonDeserializer<Any>().apply {
            // Deserialize에 대해서 신뢰하는 패키지를 지정한다. "*"를 지정하면 모두 신뢰하게 된다.
            addTrustedPackages("*")
        }

        return DefaultKafkaConsumerFactory(props, StringDeserializer(), jsonDeserializer)
    }

    // 높은 우선순위 처리를 위한 컨슈머 추가
    @Bean
    fun highPriorityKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Any> =
        ConcurrentKafkaListenerContainerFactory<String, Any>().apply {
            consumerFactory = consumerFactory("consumerGroupHighPriority") // 컨슈머 그룹이름 설정
            // 레코드가 들어오면 setRecordFilterStrategy 에 의해서 hightPriority 로 키가 없다면 필터링 처리되어서 메시지를 버리게 된다.
            setRecordFilterStrategy { consumerRecord -> "highPriority" != consumerRecord.key() }
            setConcurrency(1)
            setAutoStartup(true)
        }

    // 일반 우선순위를 위한 컨슈머 추가
    @Bean
    fun normalPriorityKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Any> =
        ConcurrentKafkaListenerContainerFactory<String, Any>().apply {
            consumerFactory = consumerFactory("consumerGroupNormalPriority") // 컨슈머 그룹이름 설정
            setRecordFilterStrategy { consumerRecord -> "highPriority" == consumerRecord.key() }
            setConcurrency(1)
            setAutoStartup(true)
        }


    @Bean
    fun errorCommonHandlingKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Any> =
        ConcurrentKafkaListenerContainerFactory<String, Any>().apply {
            consumerFactory = consumerFactory("errorHandlingConsumerGroup")
            // 기본 Error 핸들러 등록
            // SeekToCurrentErrorHandler 의 경우, 현재 읽은 오프셋에서 에러가 발생하면 FixedBackOff 등으로 설정한 backoff 만큼 기다리다가 다시 메시지를 읽는다.
            // FixedBackOff(주기(밀리세컨), 최대재시도횟수) 로 백오프를 지정했다.
            setCommonErrorHandler(DefaultErrorHandler(FixedBackOff(100, 2)))
            setConcurrency(1)
            setAutoStartup(true)
        }


    /**
     * 카프카 버전이 바뀌면서
     * 기존의 ErrorHandler 는 CommonErrorHandler 로 통합됨
     */
    @Bean
    fun errorHandlingKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Any> =
        ConcurrentKafkaListenerContainerFactory<String, Any>().apply {
            consumerFactory = consumerFactory("errorHandlingConsumerGroup")
            // SeekToCurrentErrorHandler 의 경우, 현재 읽은 오프셋에서 에러가 발생하면 FixedBackOff 등으로 설정한 backoff 만큼 기다리다가 다시 메시지를 읽는다.
            // FixedBackOff(주기(밀리세컨), 최대재시도횟수) 로 백오프를 지정했다.
            setCommonErrorHandler(DefaultErrorHandler({ record, exception ->
                logger.error("Error processing record: $record with exception: $exception")
            }))
            setConcurrency(1)
            setAutoStartup(true)
        }

    /**
     * Recovery 처리하기.
     */
    @Bean
    fun recoveryHandlingKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Any> =
        ConcurrentKafkaListenerContainerFactory<String, Any>().apply {
            consumerFactory = consumerFactory("retryHandlingConsumerGroup")
            setReplyTemplate()
        }

    private fun retryTemplate(): RetryTemplate =
        RetryTemplate().apply {
            setRetryPolicy()
        }

    private fun getSimpleRetryPolicy(): SimpleRetryPolicy {
        val exceptionMap: Map<Class<? extens Throwable>>  = mapOf(RetryTestException::class.java to true)
        return SimpleRetryPolicy(5, exceptionMap, true)
    }

}

// 재처리를 위한 Exception 정의
class RetryTestException : RuntimeException()
