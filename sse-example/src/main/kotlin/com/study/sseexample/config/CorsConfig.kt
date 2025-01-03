package com.study.sseexample.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app.cors")
data class CorsConfig(
    var origins: Array<String> = arrayOf("*"),
)
