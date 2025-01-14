package com.study.kopringbatch.config

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.support.ListItemReader
import org.springframework.context.annotation.Bean

import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

// @Configuration
class ChunkJobConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager
) {

    @Bean
    fun chunkJob(): Job {
        return JobBuilder("chunkJob", jobRepository)
            .start(chunkStep())
            .build()
    }

    @Bean
    fun chunkStep(): Step {
        return StepBuilder("chunkStep", jobRepository)
            .chunk<String, String>(10, transactionManager)
            .reader(itemReader())
            .processor(itemProcessor())
            .writer(itemWriter())
            .build()
    }

    @Bean
    fun itemReader(): ItemReader<String> {
        return ListItemReader(listOf("Spring", "Batch", "Chunk", "Processing"))
    }

    @Bean
    fun itemProcessor(): ItemProcessor<String, String> {
        return ItemProcessor { item -> item.uppercase() }
    }

    @Bean
    fun itemWriter(): ItemWriter<String> {
        return ItemWriter { items ->
            items.forEach { println("Processed Item: $it") }
        }
    }
}
