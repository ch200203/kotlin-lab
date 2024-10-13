package com.study.kopringbatch.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import javax.sql.DataSource

@SpringBootTest
@ActiveProfiles("test")
class BasicTaskJobConfigurationTest {

    @Autowired
    private lateinit var jobLauncher: JobLauncher

    @Autowired
    private lateinit var myJob: Job

    @Autowired
    private lateinit var dataSource: DataSource

    @Test
    fun testMyJob() {
        val jobParameters = JobParametersBuilder()
            .addLong("time", System.currentTimeMillis())
            .toJobParameters()

        // Job 실행
        val jobExecution: JobExecution = jobLauncher.run(myJob, jobParameters)

        // Job 상태 확인
        assertThat(jobExecution.status.isUnsuccessful).isFalse
        assertThat(jobExecution.exitStatus.exitCode).isEqualTo("COMPLETED")

        dataSource.connection.use { connection ->
            connection.prepareStatement("SELECT * FROM BATCH_JOB_EXECUTION WHERE JOB_INSTANCE_ID = ?")
                .use { statement ->
                    statement.setLong(1, jobExecution.jobInstance.id)
                    val resultSet = statement.executeQuery()
                    assertThat(resultSet.next()).isTrue  // 배치 작업 기록이 존재하는지 확인
                }
        }
    }
}
