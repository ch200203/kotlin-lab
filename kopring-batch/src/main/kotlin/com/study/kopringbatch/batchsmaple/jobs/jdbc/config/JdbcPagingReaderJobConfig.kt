package com.study.kopringbatch.batchsmaple.jobs.jdbc.config

import com.study.kopringbatch.batchsmaple.jobs.jdbc.Customer
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.database.JdbcPagingItemReader
import org.springframework.batch.item.database.Order
import org.springframework.batch.item.database.PagingQueryProvider
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean
import org.springframework.batch.item.file.FlatFileItemWriter
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource


@Configuration
class JdbcPagingReaderJobConfig(

) {

    private val log = KotlinLogging.logger { }

    companion object {
        const val CHUNK_SIZE = 2
        const val ENCODING = "UTF-8"
        const val JDBC_PAGING_CHUNK_JOB = "JDBC_PAGING_CHUNK_JOB"
    }

    @Autowired
    lateinit var dataSource: DataSource

    /**
     * 데이터 베이스에서 데이터를 읽어오는 JdbcPagingItemReader 구현
     */
    @Bean
    fun jdbcPagingItemReader(): JdbcPagingItemReader<Customer> {
        val parameterValue: MutableMap<String, Any> = HashMap()
        parameterValue["age"] = 20


        return JdbcPagingItemReaderBuilder<Customer>()
            .name("jdbcPagingItemReader")
            .fetchSize(CHUNK_SIZE)
            .dataSource(dataSource)
            .rowMapper(BeanPropertyRowMapper<Customer>(Customer::class.java))
            .queryProvider(queryProvider())
            .parameterValues(parameterValue)
            .build()
    }

    // QueryProvider 에 따라 데이터를 가져온다 -> 여기서는 20세 이상인 경우
    @Bean
    @Throws(Exception::class)
    fun queryProvider(): PagingQueryProvider {
        val queryProvider = SqlPagingQueryProviderFactoryBean()
        queryProvider.setDataSource(dataSource) // DB에 맞는 PagingQueryProvider를 선택하기 위함
        queryProvider.setSelectClause("id, name, age, gender")
        queryProvider.setFromClause("from customer")
        queryProvider.setWhereClause("where age >= :age")

        val sortKeys = mapOf("id" to Order.DESCENDING)
        queryProvider.setSortKeys(sortKeys)

        return queryProvider.`object`
    }

    /**
     * FlatFileItemWriter를 사용하여 데이터를 파일로 작성
     * 출력 파일 경로는 ./output/customer_new_v1.csv로 설정되며, 구분자는 탭(\t)
     */
    @Bean
    fun customerFlatFileItemWriter(): FlatFileItemWriter<Customer> {
        return FlatFileItemWriterBuilder<Customer>()
            .name("customerFlatFileItemWriter")
            .resource(FileSystemResource("./output/customer_new_v1.csv"))
            .encoding(ENCODING)
            .delimited().delimiter("\t")
            .names("Name", "Age", "Gender")
            .build()
    }


    @Bean
    @Throws(Exception::class)
    fun customerJdbcPagingStep(jobRepository: JobRepository, transactionManager: PlatformTransactionManager): Step {
        log.info { "------------------ Init customerJdbcPagingStep -----------------" }

        return StepBuilder("customerJdbcPagingStep", jobRepository)
            .chunk<Customer, Customer>(CHUNK_SIZE, transactionManager)
            .reader(jdbcPagingItemReader())
            .writer(customerFlatFileItemWriter())
            .build()
    }

    @Bean
    fun customerJdbcPagingJob(customerJdbcPagingStep: Step, jobRepository: JobRepository): Job {
        log.info { "------------------ Init customerJdbcPagingJob -----------------" }
        return JobBuilder(JDBC_PAGING_CHUNK_JOB, jobRepository)
            .incrementer(RunIdIncrementer())
            .start(customerJdbcPagingStep)
            .build()
    }

}
