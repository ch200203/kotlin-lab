package com.study.batchquiz.config

import com.study.batchquiz.CsvProcessor
import com.study.batchquiz.reader.CsvReader
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class FlatFileBatchConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val csvReader: CsvReader,
    private val csvProcessor: CsvProcessor,
) {


    @Bean
    fun csvJob(): Job = JobBuilder("ciReportJob", jobRepository)
        .incrementer(RunIdIncrementer())
        .start(csvStep())
        .build()


    @Bean
    fun csvStep(): Step {
        return StepBuilder("csvStep", jobRepository)
            .chunk<Map<String, String>, Map<String, String>>(10, transactionManager)
            .reader(csvReader.reader())
            .processor(csvProcessor.processor())
            .writer(csvWriter.writer())
            .build()
    }
}
