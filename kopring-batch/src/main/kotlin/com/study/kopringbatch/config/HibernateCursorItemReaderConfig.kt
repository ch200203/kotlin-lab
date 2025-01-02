package com.study.kopringbatch.config

import com.study.kopringbatch.domain.Order
import jakarta.persistence.EntityManagerFactory
import org.hibernate.SessionFactory
import org.springframework.batch.item.database.HibernateCursorItemReader
import org.springframework.batch.item.database.builder.HibernateCursorItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// @Configuration
class HibernateCursorItemReaderConfig(
    private val entityManagerFactory: EntityManagerFactory
) {
    @Bean
    fun hibernateCursorItemReader(): HibernateCursorItemReader<Order> {
        return HibernateCursorItemReaderBuilder<Order>()
            .name("hibernateCursorItemReader")
            .sessionFactory(entityManagerFactory.unwrap(SessionFactory::class.java))
            .queryString("FROM Order")
            .build()
    }
}
