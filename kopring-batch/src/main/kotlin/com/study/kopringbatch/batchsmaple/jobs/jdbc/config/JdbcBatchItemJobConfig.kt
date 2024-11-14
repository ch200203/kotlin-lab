package com.study.kopringbatch.batchsmaple.jobs.jdbc.config

import com.study.kopringbatch.domain.Customer
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
class JdbcBatchItemJobConfig {

    private val log = KotlinLogging.logger { }

    companion object {
        /**
         * CHUNK 크기를 지정한다.
         */
        const val CHUNK_SIZE = 100
        const val ENCODING = "UTF-8"
        const val JDBC_BATCH_WRITER_CHUNK_JOB = "JDBC_BATCH_WRITER_CHUNK_JOB"
    }

    @Autowired
    lateinit var dataSource: DataSource

    @Bean
    fun flatFileItemReader(): FlatFileItemReader<Customer> {
        return FlatFileItemReaderBuilder<Customer>()
            .name("FlatFileItemReader")
            .resource(ClassPathResource("./customer.csv"))
            .encoding(ENCODING)
            .delimited()
            .delimiter(",")
            .names("name", "age", "gender")
            .targetType(Customer::class.java)
            .build()
    }

    @Bean
    fun flatFileItemWriter(): JdbcBatchItemWriter<Customer> {
        return JdbcBatchItemWriterBuilder<Customer>()
            .dataSource(dataSource)
            .sql("INSERT INTO customer2 (name, age, gender) VALUES (:name, :age, :gender)")
            .itemSqlParameterSourceProvider(CustomerItemSqlParameterSourceProvider())
            .build()
    }

    @Bean
    fun flatFileStep(jobRepository: JobRepository, transactionManager: PlatformTransactionManager): Step {
        log.info { "------------------ Init flatFileStep -----------------" }

        return StepBuilder("flatFileStep", jobRepository)
            .chunk<Customer, Customer>(CHUNK_SIZE, transactionManager)
            .reader(flatFileItemReader())
            .writer(flatFileItemWriter())
            .build()
    }

    @Bean
    fun flatFileJob(flatFileStep: Step, jobRepository: JobRepository): Job {
        log.info{ "------------------ Init flatFileJob -----------------" }
        return JobBuilder(JDBC_BATCH_WRITER_CHUNK_JOB, jobRepository)
            .incrementer(RunIdIncrementer())
            .start(flatFileStep)
            .build()
    }
}
