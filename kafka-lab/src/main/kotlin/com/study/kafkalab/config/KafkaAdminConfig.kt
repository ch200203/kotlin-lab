package com.study.kafkalab.config

import org.apache.kafka.clients.admin.AdminClientConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaAdmin


@Configuration
class KafkaAdminConfig {

    @Value("\${kafka.bootstrap-servers}")
    private lateinit var bootstrapServer: String

    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val configs: MutableMap<String, Any> = HashMap()
        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServer!!

        return KafkaAdmin(configs)
    }
}

