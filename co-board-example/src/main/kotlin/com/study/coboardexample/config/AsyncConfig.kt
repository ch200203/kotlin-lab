package com.study.coboardexample.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executors


@Configuration
@EnableAsync
class AsyncConfig {

    // async 메소드에서 MDC 을 통해 기존 스레드에서 설정한 값을 호출해보면 이전과 다르게 null 이 아니라 값이 들어있음
    @Bean
    fun taskExecutor(): TaskExecutor {
        val taskExecutor = ThreadPoolTaskExecutor()

        // corePool -> 기본 스레드 풀 개수
        // maxPool -> 최대 개수
        // queueCapacity 가 맥스이상의 업무가 몰려오면 큐 개수만큼은 저장해둘 수 있다는 뜻. 단, 그 이상이 들어오면 탈락시켜버린다.

        taskExecutor.corePoolSize = 20
        taskExecutor.maxPoolSize = 50
        taskExecutor.queueCapacity = 200
        taskExecutor.maxPoolSize = 50

        taskExecutor.setTaskDecorator(ClonedTaskDecorator())
        taskExecutor.setThreadNamePrefix("async-task-")
        taskExecutor.setThreadGroupName("async-group")

        return taskExecutor
    }
}
