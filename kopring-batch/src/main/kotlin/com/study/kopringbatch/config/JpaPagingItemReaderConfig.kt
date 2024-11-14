package com.study.kopringbatch.config

import com.study.kopringbatch.domain.Order
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JpaPagingItemReaderConfig(
    private val entityManagerFactory: EntityManagerFactory
) {
    @Bean
    fun jpaPagingItemReader(): JpaPagingItemReader<Order> {
        return JpaPagingItemReaderBuilder<Order>()
            .name("jpaPagingItemReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT o FROM Order o")
            .pageSize(1000)
            .build()
    }

}
