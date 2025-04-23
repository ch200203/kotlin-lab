package com.study.kafkalab.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
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
        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"

        val jsonDeserializer = JsonDeserializer(Any::class.java).apply {
            addTrustedPackages("*")
        }

        return DefaultKafkaConsumerFactory(props, StringDeserializer(), jsonDeserializer)
    }

    @Bean
    fun highPriorityKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Any> =
        ConcurrentKafkaListenerContainerFactory<String, Any>().apply {
            consumerFactory = consumerFactory("consumerGroupHighPriority")
            setRecordFilterStrategy { consumerRecord -> consumerRecord.key() != "highPriority" }
            setConcurrency(1)
            setAutoStartup(true)
        }

    @Bean
    fun normalPriorityKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Any> =
        ConcurrentKafkaListenerContainerFactory<String, Any>().apply {
            consumerFactory = consumerFactory("consumerGroupNormalPriority")
            setRecordFilterStrategy { consumerRecord -> consumerRecord.key() == "highPriority" }
            setConcurrency(1)
            setAutoStartup(true)
        }

    @Bean
    fun errorCommonHandlingKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Any> =
        ConcurrentKafkaListenerContainerFactory<String, Any>().apply {
            consumerFactory = consumerFactory("errorHandlingConsumerGroup")
            setCommonErrorHandler(DefaultErrorHandler(FixedBackOff(100, 2)))
            setConcurrency(1)
            setAutoStartup(true)
        }

    @Bean
    fun errorHandlingKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Any> =
        ConcurrentKafkaListenerContainerFactory<String, Any>().apply {
            consumerFactory = consumerFactory("errorHandlingConsumerGroup")
            setCommonErrorHandler(
                DefaultErrorHandler({ record, exception ->
                    logger.error("Error processing record: $record with exception: $exception")
                })
            )
            setConcurrency(1)
            setAutoStartup(true)
        }

    /**
     * Recovery 처리: 재시도 최대 5회 후, Recovery 콜백에서 로그 출력
     */
    @Bean
    fun recoveryHandlingKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Any> =
        ConcurrentKafkaListenerContainerFactory<String, Any>().apply {
            consumerFactory = consumerFactory("retryHandlingConsumerGroup")

            val errorHandler = DefaultErrorHandler(
                { record: ConsumerRecord<*, *>, ex: Exception ->
                    logger.warn("Recovery callback triggered. Message: ${record.value()}, exception: ${ex.message}")
                },
                FixedBackOff(100L, 4L) // 총 5번 시도 (초기 + 4회 재시도)
            )

            errorHandler.addRetryableExceptions(RetryTestException::class.java)

            setCommonErrorHandler(errorHandler)
            setConcurrency(1)
            setAutoStartup(true)
        }
}

// 재처리를 위한 Exception 정의
class RetryTestException : RuntimeException()
