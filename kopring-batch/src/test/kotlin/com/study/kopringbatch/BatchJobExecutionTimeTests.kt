package com.study.kopringbatch

import com.study.kopringbatch.config.JobConfig
import org.aspectj.lang.annotation.Before
import org.junit.jupiter.api.Test
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import javax.sql.DataSource
import kotlin.system.measureNanoTime

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = [JobConfig::class])
class BatchJobExecutionTimeTests {

    @Autowired
    private lateinit var jobLauncher: JobLauncher

    @Autowired
    private lateinit var jdbcCursorJob: Job

    @Autowired
    private lateinit var hibernateCursorJob: Job

    @Autowired
    private lateinit var jpaPagingJob: Job

    private fun getUniqueJobParameters(): JobParameters {
        return JobParametersBuilder()
            .addLong("timestamp", System.currentTimeMillis())
            .toJobParameters()
    }

    @Test
    fun testJdbcCursorJobExecutionTime() {
        val executionTime = measureNanoTime {
            val jobExecution = jobLauncher.run(jdbcCursorJob, getUniqueJobParameters())
            println("JdbcCursorJob Status: ${jobExecution.status}")
        }
        println("JdbcCursorJob 실행시간 : ${executionTime / 1_000_000} ms")
    }

    @Test
    fun testHibernateCursorJobExecutionTime() {
        val executionTime = measureNanoTime {
            val jobExecution = jobLauncher.run(hibernateCursorJob, getUniqueJobParameters())
            println("HibernateCursorJob Status: ${jobExecution.status}")
        }
        println("HibernateCursorJob 실행시간: ${executionTime / 1_000_000} ms")
    }

    @Test
    fun testJpaPagingJobExecutionTime() {
        val executionTime = measureNanoTime {
            val jobExecution = jobLauncher.run(jpaPagingJob, getUniqueJobParameters())
            println("JpaPagingJob Status: ${jobExecution.status}")
        }
        println("JpaPagingJob 실행시간: ${executionTime / 1_000_000} ms")
    }
}
