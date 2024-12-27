package com.study.kafkalab.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory


@EnableKafka
@Configuration
class KafkaConsumerConfig {

    @Value("\${kafka.bootstrap-servers}")
    private lateinit var bootstrapServer: String


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

    @Bean
    fun defaultKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Any> =
        ConcurrentKafkaListenerContainerFactory<String, Any>().apply {
            consumerFactory = consumerFactory("defaultGroup")
            concurrency = 1
            // isAutoStartup = trues
        }
}