package com.study.kopringbatch.config

import com.study.kopringbatch.domain.Order
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.item.database.JpaCursorItemReader
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JpaCursorItemReaderConfig(
    private val entityManagerFactory: EntityManagerFactory
) {

    @Bean
    fun jpaCursorItemReader(entityManagerFactory: EntityManagerFactory): JpaCursorItemReader<Order> {
        return JpaCursorItemReaderBuilder<Order>()
            .name("jpaCursorItemReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT o FROM Order o")
            .build()
    }
}
