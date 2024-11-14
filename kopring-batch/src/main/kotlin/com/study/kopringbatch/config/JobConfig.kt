package com.study.kopringbatch.config

import com.study.kopringbatch.domain.Order
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.HibernateCursorItemReader
import org.springframework.batch.item.database.JdbcCursorItemReader
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class JobConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val jdbcCursorItemReader: JdbcCursorItemReader<Order>,
    private val hibernateCursorItemReader: HibernateCursorItemReader<Order>,
    private val jpaPagingItemReader: JpaPagingItemReader<Order>,
) {

    @Bean
    fun jdbcCursorJob(): Job {
        return JobBuilder("jdbcCursorJob", jobRepository)
            .start(jdbcCursorStep())
            .build()
    }

    @Bean
    fun jdbcCursorStep(): Step {
        return StepBuilder("jdbcCursorStep", jobRepository)
            .chunk<Order, Order>(1000, transactionManager)
            .reader(jdbcCursorItemReader)
            .writer(dummyWriter())
            .build()
    }

    @Bean
    fun hibernateCursorJob(): Job {
        return JobBuilder("hibernateCursorJob", jobRepository)
            .start(hibernateCursorStep())
            .build()
    }

    @Bean
    fun hibernateCursorStep(): Step {
        return StepBuilder("hibernateCursorStep", jobRepository)
            .chunk<Order, Order>(1000, transactionManager)
            .reader(hibernateCursorItemReader)
            .writer(dummyWriter())
            .build()
    }

    @Bean
    fun jpaPagingJob(): Job {
        return JobBuilder("jpaPagingJob", jobRepository)
            .start(jpaPagingStep())
            .build()
    }

    @Bean
    fun jpaPagingStep(): Step {
        return StepBuilder("jpaPagingStep", jobRepository)
            .chunk<Order, Order>(1000, transactionManager)
            .reader(jpaPagingItemReader)
            .writer(dummyWriter())
            .build()
    }

    // 라이터는 print 로 대체
    @Bean
    fun dummyWriter(): ItemWriter<Order> {
        return ItemWriter { it -> println("Writing ${it.size()} items") }
    }
}
