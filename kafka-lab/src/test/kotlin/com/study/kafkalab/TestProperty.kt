package com.study.kafkalab

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "test.value")
data class TestProperty(
    val value: String
)