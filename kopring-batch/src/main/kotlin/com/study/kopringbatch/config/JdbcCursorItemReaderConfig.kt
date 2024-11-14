package com.study.kopringbatch.config

import com.study.kopringbatch.domain.Order
import org.springframework.batch.item.database.JdbcCursorItemReader
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.BeanPropertyRowMapper
import javax.sql.DataSource

@Configuration
class JdbcCursorItemReaderConfig(
    private val dataSource: DataSource
) {
    @Bean
    fun jdbcCursorItemReader(): JdbcCursorItemReader<Order> {
        return JdbcCursorItemReaderBuilder<Order>()
            .name("jdbcCursorItemReader")
            .dataSource(dataSource)
            .sql("SELECT id, name, quantity, category FROM Order o")
            .rowMapper(BeanPropertyRowMapper(Order::class.java))
            .fetchSize(1000)
            .build()
    }
}
