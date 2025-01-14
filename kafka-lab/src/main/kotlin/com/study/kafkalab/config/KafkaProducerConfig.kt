package com.study.kafkalab.config

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer


@Configuration
class KafkaProducerConfig {

    @Value("\${kafka.bootstrap-servers}")
    private val bootstrapServer: String? = null

    private fun producerFactory(): ProducerFactory<String, Any> {
        val configProps: MutableMap<String, Any?> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServer
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        // 주의할점, com.fasterxml.jackson.databind 가 아닌, kafkaJsonSerializer 를 사용해야함.
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java // kafka 메시지로 전송된 값을 직렬화 할 방식 설정

        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun kafkaProducerTemplate(): KafkaTemplate<String, Any> {
        return KafkaTemplate(producerFactory())
    }

}